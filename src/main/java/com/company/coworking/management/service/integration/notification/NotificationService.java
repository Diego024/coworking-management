package com.company.coworking.management.service.integration.notification;

import com.company.coworking.management.entity.Reservation;

public interface NotificationService {
    void sendReservationConfirmation(Reservation reservation);
}
