package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.validation.reservation.ReservationValidationChain;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidateReservationStep implements ReservationStep {

    private final ReservationValidationChain validationChain;

    @Override
    public void execute(CreateReservationContext context) {
        validationChain.execute(context);
    }
}