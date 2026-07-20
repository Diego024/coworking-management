package com.company.coworking.management.service.integration.notification;

import com.company.coworking.management.entity.Reservation;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class NotificationServiceImpl implements NotificationService {

    @Override
    public void sendReservationConfirmation(Reservation reservation) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("""
                        ==========================================================
                        Reservation confirmation email sent.
                        Reservation: {}
                        User: {}
                        Email: {}
                        ==========================================================
                        """,
                reservation.getId(),
                reservation.getUser().getId(),
                reservation.getUser().getEmail());

    }

}