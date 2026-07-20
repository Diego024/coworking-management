package com.company.coworking.management.service.impl;

import com.company.coworking.management.dto.response.SpaceOccupancyReportResponse;
import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.entity.Space;
import com.company.coworking.management.exception.business.InvalidDateRangeException;
import com.company.coworking.management.repository.ReservationRepository;
import com.company.coworking.management.repository.SpaceRepository;
import com.company.coworking.management.service.SpaceReportsService;
import com.company.coworking.management.mapper.SpaceOccupancyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class SpaceReportsServiceImpl implements SpaceReportsService {

    private static final BigDecimal ONE_HUNDRED = new BigDecimal("100.00");

    private final SpaceRepository spaceRepository;
    private final ReservationRepository reservationRepository;
    private final SpaceOccupancyMapper spaceOccupancyMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(
            value = "space-occupancy",
            key = "#startDate.toString() + '-' + #endDate.toString()"
    )
    public List<SpaceOccupancyReportResponse> getSpaceOccupancy(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Generating occupancy report from {} to {}", startDate, endDate);

        validateDateRange(startDate, endDate);

        List<Space> spaces = spaceRepository.findAll();

        Map<Long, List<Reservation>> reservationsBySpace = reservationRepository
                .findReservationsForOccupancyReport(startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(reservation -> reservation.getSpace().getId()));

        return spaces.stream()
                .map(space -> buildSpaceOccupancyResponse(space, reservationsBySpace, startDate, endDate))
                .toList();
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if (!startDate.isBefore(endDate)) {
            throw new InvalidDateRangeException("Start date must be strictly before end date");
        }
    }

    private SpaceOccupancyReportResponse buildSpaceOccupancyResponse(
            Space space,
            Map<Long, List<Reservation>> reservationsBySpace,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        List<Reservation> spaceReservations = reservationsBySpace.getOrDefault(space.getId(), Collections.emptyList());
        BigDecimal occupancyPercentage = calculateOccupancyPercentage(spaceReservations, startDate, endDate);

        return spaceOccupancyMapper.toResponse(space, occupancyPercentage);
    }

    private BigDecimal calculateOccupancyPercentage(Collection<Reservation> reservations, LocalDateTime reportStart, LocalDateTime reportEnd) {
        long totalReportMinutes = Duration.between(reportStart, reportEnd).toMinutes();

        if (totalReportMinutes == 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long totalReservedMinutes = reservations.stream()
                .mapToLong(reservation -> calculateOverlappingMinutes(reservation, reportStart, reportEnd))
                .sum();

        return BigDecimal.valueOf(totalReservedMinutes)
                .multiply(ONE_HUNDRED)
                .divide(BigDecimal.valueOf(totalReportMinutes), 2, RoundingMode.HALF_UP);
    }

    private long calculateOverlappingMinutes(Reservation reservation, LocalDateTime reportStart, LocalDateTime reportEnd) {
        LocalDateTime effectiveStart = reservation.getStartTime().isAfter(reportStart)
                ? reservation.getStartTime()
                : reportStart;

        LocalDateTime effectiveEnd = reservation.getEndTime().isBefore(reportEnd)
                ? reservation.getEndTime()
                : reportEnd;

        return Math.max(0, Duration.between(effectiveStart, effectiveEnd).toMinutes());
    }
}