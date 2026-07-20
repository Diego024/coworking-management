package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.event.ReservationConfirmedEvent;
import com.company.coworking.management.integration.payment.dto.PaymentValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishReservationEventStep implements ReservationStep {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(CreateReservationContext context) {

        if (PaymentValidationResult.APPROVED.equals(context.getPaymentResult())) {
            applicationEventPublisher.publishEvent(
                    new ReservationConfirmedEvent(context.getReservation())
            );
        }
    }
}