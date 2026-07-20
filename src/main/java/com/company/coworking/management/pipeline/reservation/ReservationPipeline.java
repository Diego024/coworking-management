package com.company.coworking.management.pipeline.reservation;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.pipeline.reservation.step.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
public class ReservationPipeline {

    private final List<ReservationStep> steps;

    public ReservationPipeline(
            LoadUserStep loadUserStep,
            LoadSpaceStep loadSpaceStep,
            ValidateReservationStep validateReservationStep,
            CalculateReservationPriceStep calculateReservationPriceStep,
            ValidatePaymentStep validatePaymentStep,
            CreateReservationStep createReservationStep
//            PublishReservationEventStep publishReservationEventStep
    ) {
        this.steps = List.of(
                loadUserStep,
                loadSpaceStep,
                validateReservationStep,
                calculateReservationPriceStep,
                validatePaymentStep,
                createReservationStep
//                publishReservationEventStep
        );
    }

    public void execute(CreateReservationContext context) {
        log.info("Starting reservation pipeline for user: {}", context.getRequest().getUserId());

        for (ReservationStep step : steps) {
            log.debug("Executing step: {}", step.getClass().getSimpleName());
            step.execute(context);
        }

        log.info("Reservation pipeline completed successfully. Reservation ID: {}",
                context.getReservation().getId());
    }
}