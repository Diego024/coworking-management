package com.company.coworking.management.exception.business;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long reservationId) {
        super("Could not find reservation with id " + reservationId);
    }
}
