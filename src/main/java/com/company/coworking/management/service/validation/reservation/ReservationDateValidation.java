package com.company.coworking.management.service.validation.reservation;

import com.company.coworking.management.exception.business.InvalidDateRangeException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@Order(ReservationValidationOrder.DATE_RANGE)
public class ReservationDateValidation implements ReservationValidation {

    @Override
    public void validate(CreateReservationContext context) {
        LocalDateTime startTime = context.getRequest().getStartTime();
        LocalDateTime endTime = context.getRequest().getEndTime();

        if (startTime.isBefore(LocalDateTime.now(ZoneId.systemDefault()))) {
            throw new InvalidDateRangeException("Reservation start time cannot be in the past.");
        }

        if (!endTime.isAfter(startTime)) {
            throw new InvalidDateRangeException("Reservation end time must be strictly after start time.");
        }
    }
}