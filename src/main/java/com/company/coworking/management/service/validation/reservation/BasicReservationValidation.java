package com.company.coworking.management.service.validation.reservation;

import com.company.coworking.management.exception.business.InvalidReservationDurationException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@Order(ReservationValidationOrder.BASIC_DATA)
public class BasicReservationValidation implements ReservationValidation {

    @Override
    public void validate(CreateReservationContext context) {
        long minutes = Duration.between(context.getRequest().getStartTime(), context.getRequest().getEndTime()).toMinutes();

        if (minutes < 30) {
            throw new InvalidReservationDurationException("Reservation must be at least 30 minutes long.");
        }
    }
}