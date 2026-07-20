package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.event.ReservationCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PublishReservationEventStep implements ReservationStep {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void execute(CreateReservationContext context) {

        applicationEventPublisher.publishEvent(
                new ReservationCreatedEvent(context.getReservation())
        );
    }
}