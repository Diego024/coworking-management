package com.company.coworking.management.integration.notification;

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
                        \s
                        ==========================================================
                        Reservation confirmation email sent.
                        Reservation: {}
                        User: {}
                        Space: {}
                        ==========================================================
                        """,
                reservation.getId(),
                reservation.getUser().getEmail(),
                reservation.getSpace().getName());

    }
}