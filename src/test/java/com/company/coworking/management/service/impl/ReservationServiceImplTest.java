package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.ReservationResponse;
import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.entity.User;
import com.company.coworking.management.enums.ReservationStatus;
import com.company.coworking.management.exception.business.ReservationCannotBeCancelledException;
import com.company.coworking.management.mapper.ReservationMapper;
import com.company.coworking.management.pipeline.reservation.ReservationPipeline;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.repository.ReservationRepository;
import com.company.coworking.management.service.CurrentUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationPipeline reservationPipeline;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Spy
    private ReservationMapper reservationMapper = Mappers.getMapper(ReservationMapper.class);

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Test
    void createReservation_ShouldExecutePipelineAndMapResponse() {
        CreateReservationRequest request = CreateReservationRequest.builder().build();
        Reservation reservation = new Reservation();
        ReservationResponse expectedResponse = new ReservationResponse();

        org.mockito.Mockito.doAnswer(invocation -> {
            CreateReservationContext context = invocation.getArgument(0);
            context.setUser(User.builder().email("user@coworking.com").build());
            context.setSpace(com.company.coworking.management.entity.Space.builder().name("Desk 01").build());
            context.setReservation(reservation);
            return null;
        }).when(reservationPipeline).execute(any(CreateReservationContext.class));

        reservation.setUser(User.builder().email("user@coworking.com").build());
        reservation.setSpace(com.company.coworking.management.entity.Space.builder().name("Desk 01").build());

        ReservationResponse response = reservationService.createReservation(request);

        Assertions.assertEquals("user@coworking.com", response.getUser());
        Assertions.assertEquals("Desk 01", response.getSpaceName());
        verify(reservationPipeline).execute(any(CreateReservationContext.class));
    }

    @Test
    void getCurrentUserReservations_ShouldUseAuthenticatedUserAndMapOnlyTheirReservations() {
        User mockUser = new User();
        mockUser.setId(1L);

        Reservation reservation = new Reservation();
        reservation.setUser(User.builder().email("user@coworking.com").build());
        reservation.setSpace(com.company.coworking.management.entity.Space.builder().name("Desk 01").build());
        List<Reservation> mockReservations = List.of(reservation);

        when(currentUserService.getCurrentUser()).thenReturn(mockUser);
        when(reservationRepository.findByUserId(1L)).thenReturn(mockReservations);

        List<ReservationResponse> response = reservationService.getCurrentUserReservations();

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals("user@coworking.com", response.get(0).getUser());
        Assertions.assertEquals("Desk 01", response.get(0).getSpaceName());
        verify(currentUserService).getCurrentUser();
        verify(reservationRepository, times(1)).findByUserId(1L);
    }

    @Test
    void cancelReservation_ShouldChangeStatusToCancelled_SaveAndReturnResponse() {
        Long reservationId = 100L;
        Long userId = 1L;

        Reservation mockReservation = new Reservation();
        mockReservation.setId(reservationId);
        mockReservation.setStatus(ReservationStatus.PENDING_PAYMENT);
        mockReservation.setUser(User.builder().email("user@coworking.com").build());
        mockReservation.setSpace(com.company.coworking.management.entity.Space.builder().name("Desk 01").build());

        when(currentUserService.isAdmin()).thenReturn(false);
        when(currentUserService.getCurrentUserId()).thenReturn(userId);
        when(reservationRepository.findByIdAndUserId(reservationId, userId)).thenReturn(Optional.of(mockReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(mockReservation);

        ReservationResponse result = reservationService.cancelReservation(reservationId);

        Assertions.assertEquals(ReservationStatus.CANCELLED, result.getStatus());
        Assertions.assertEquals("Desk 01", result.getSpaceName());
        Assertions.assertEquals(ReservationStatus.CANCELLED, mockReservation.getStatus());
        verify(reservationRepository).save(mockReservation);
    }

    @Test
    void cancelReservation_WhenReservationIsNotCancelable_ShouldThrowException() {
        Long reservationId = 100L;
        Reservation mockReservation = new Reservation();
        mockReservation.setId(reservationId);
        mockReservation.setStatus(ReservationStatus.COMPLETED);

        when(currentUserService.isAdmin()).thenReturn(true);
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(mockReservation));

        Assertions.assertThrows(ReservationCannotBeCancelledException.class,
                () -> reservationService.cancelReservation(reservationId));

        verify(reservationRepository, never()).save(any());
    }
}