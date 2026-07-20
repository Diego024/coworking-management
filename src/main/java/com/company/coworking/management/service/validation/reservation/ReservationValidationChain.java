package com.company.coworking.management.service.validation.reservation;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class ReservationValidationChain {

    private final List<ReservationValidation> validations;

    public void execute(CreateReservationContext context) {
        log.info("Starting reservation validation chain for user: {}", context.getRequest().getUserId());

        for (ReservationValidation validation : validations) {
            log.debug("Executing validation: {}", validation.getClass().getSimpleName());
            validation.validate(context);
        }

        log.info("Reservation validation chain completed successfully.");
    }
}