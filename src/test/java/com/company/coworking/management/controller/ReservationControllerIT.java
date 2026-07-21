package com.company.coworking.management.controller;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.entity.Space;
import com.company.coworking.management.entity.User;
import com.company.coworking.management.enums.PaymentMethod;
import com.company.coworking.management.enums.ReservationStatus;
import com.company.coworking.management.enums.Role;
import com.company.coworking.management.enums.SpaceType;
import com.company.coworking.management.integration.payment.PaymentGatewayClient;
import com.company.coworking.management.integration.payment.dto.PaymentValidationResult;
import com.company.coworking.management.repository.ReservationRepository;
import com.company.coworking.management.repository.SpaceRepository;
import com.company.coworking.management.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@EnabledIf("dockerAvailable")
class ReservationControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("coworking_test")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.flyway.enabled", () -> "true");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private SpaceRepository spaceRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private PaymentGatewayClient paymentGatewayClient;

    @BeforeEach
    void setUp() {
        reservationRepository.deleteAll();
        spaceRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@coworking.com", roles = "USER")
    void createReservation_ShouldPersistConfirmedReservationAndReturn201() throws Exception {
        seedUser("user@coworking.com");
        Space space = seedSpace();

        LocalDateTime startTime = futureStartTime();
        LocalDateTime endTime = startTime.plusHours(3);

        when(paymentGatewayClient.processPayment(any())).thenReturn(PaymentValidationResult.APPROVED);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest(space.getId(), startTime, endTime))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.data.status").value(ReservationStatus.CONFIRMED.name()))
                .andExpect(jsonPath("$.data.totalPrice").value(45.00));

        Reservation savedReservation = reservationRepository.findAll().get(0);
        Assertions.assertEquals(1L, reservationRepository.count());
        Assertions.assertEquals(ReservationStatus.CONFIRMED, savedReservation.getStatus());
        Assertions.assertEquals(new BigDecimal("45.00"), savedReservation.getTotalPrice());
        Assertions.assertEquals(space.getId(), savedReservation.getSpace().getId());
        verify(paymentGatewayClient, times(1)).processPayment(any());
    }

    @Test
    @WithMockUser(username = "user@coworking.com", roles = "USER")
    void createReservation_WhenSpaceIsOverlapping_ShouldReturn409AndKeepSingleRecord() throws Exception {
        seedUser("user@coworking.com");
        Space space = seedSpace();

        LocalDateTime startTime = futureStartTime();
        LocalDateTime endTime = startTime.plusHours(3);

        when(paymentGatewayClient.processPayment(any())).thenReturn(PaymentValidationResult.APPROVED);

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest(space.getId(), startTime, endTime))))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(buildRequest(space.getId(), startTime, endTime))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        Assertions.assertEquals(1L, reservationRepository.count());
        verify(paymentGatewayClient, times(1)).processPayment(any());
    }

    private User seedUser(String email) {
        return userRepository.save(User.builder()
                .firstName("Test")
                .lastName("User")
                .email(email)
                .password("secret")
                .role(Role.USER)
                .enabled(true)
                .build());
    }

    private Space seedSpace() {
        return spaceRepository.save(Space.builder()
                .name("Desk 01")
                .type(SpaceType.DESK)
                .capacity(1)
                .location("Floor 1")
                .hourlyRate(new BigDecimal("15.00"))
                .build());
    }

    private CreateReservationRequest buildRequest(Long spaceId, LocalDateTime startTime, LocalDateTime endTime) {
        return CreateReservationRequest.builder()
                .spaceId(spaceId)
                .startTime(startTime)
                .endTime(endTime)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();
    }

    private LocalDateTime futureStartTime() {
        return LocalDateTime.now()
                .plusDays(1)
                .withHour(10)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    static boolean dockerAvailable() {
        try {
            return org.testcontainers.DockerClientFactory.instance().isDockerAvailable();
        } catch (Exception ex) {
            return false;
        }
    }
}