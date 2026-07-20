package com.company.coworking.management.service.listener;

import com.company.coworking.management.service.event.ReservationConfirmedEvent;
import com.company.coworking.management.service.integration.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class ReservationNotificationListener {

    private final NotificationService notificationService;

    @Async("notificationExecutor")
    @EventListener
    public void handleReservationCreated(ReservationConfirmedEvent event) {
        log.info("Processing asynchronous notification for reservation {}", event.getReservation().getId());

        notificationService.sendReservationConfirmation(event.getReservation());
    }
}