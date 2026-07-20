package com.company.coworking.management.exception.business;

public class ReservationCannotBeCancelledException extends RuntimeException {
    public ReservationCannotBeCancelledException(Long reservationId) {
        super("Reservation with id " + reservationId + " cannot be cancelled.");
    }
}
