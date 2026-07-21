package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.response.SpaceOccupancyReportResponse;
import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.entity.Space;
import com.company.coworking.management.enums.SpaceType;
import com.company.coworking.management.exception.business.InvalidDateRangeException;
import com.company.coworking.management.mapper.SpaceOccupancyMapper;
import com.company.coworking.management.repository.ReservationRepository;
import com.company.coworking.management.repository.SpaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SpaceReportsServiceImplTest {

    @Mock
    private SpaceRepository spaceRepository;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SpaceOccupancyMapper spaceOccupancyMapper;

    @InjectMocks
    private SpaceReportsServiceImpl spaceReportsService;

    @Test
    void getSpaceOccupancy_ShouldCalculateFiftyPercentOccupancy() {
        LocalDateTime reportStart = LocalDateTime.of(2026, 7, 21, 8, 0);
        LocalDateTime reportEnd = LocalDateTime.of(2026, 7, 21, 18, 0);

        Space space = Space.builder()
                .id(1L)
                .name("Desk 01")
                .type(SpaceType.DESK)
                .build();

        Reservation reservation = Reservation.builder()
                .space(space)
                .startTime(LocalDateTime.of(2026, 7, 21, 8, 0))
                .endTime(LocalDateTime.of(2026, 7, 21, 13, 0))
                .build();

        when(spaceRepository.findAll()).thenReturn(List.of(space));
        when(reservationRepository.findReservationsForOccupancyReport(reportStart, reportEnd)).thenReturn(List.of(reservation));
        when(spaceOccupancyMapper.toResponse(eq(space), eq(new BigDecimal("50.00")))).thenAnswer(invocation ->
                SpaceOccupancyReportResponse.builder()
                        .spaceId(space.getId())
                        .spaceName(space.getName())
                        .spaceType(space.getType())
                        .occupancyPercentage(invocation.getArgument(1))
                        .build()
        );

        List<SpaceOccupancyReportResponse> response = spaceReportsService.getSpaceOccupancy(reportStart, reportEnd);

        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(new BigDecimal("50.00"), response.get(0).getOccupancyPercentage());
        verify(spaceOccupancyMapper).toResponse(space, new BigDecimal("50.00"));
    }

    @Test
    void getSpaceOccupancy_WhenDateRangeIsInvalid_ShouldThrowException() {
        LocalDateTime reportStart = LocalDateTime.of(2026, 7, 21, 18, 0);
        LocalDateTime reportEnd = LocalDateTime.of(2026, 7, 21, 8, 0);

        Assertions.assertThrows(InvalidDateRangeException.class,
                () -> spaceReportsService.getSpaceOccupancy(reportStart, reportEnd));

        verifyNoInteractions(spaceRepository, reservationRepository, spaceOccupancyMapper);
    }
}