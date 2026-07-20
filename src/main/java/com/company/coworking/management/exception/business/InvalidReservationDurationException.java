package com.company.coworking.management.exception.business;

public class InvalidReservationDurationException extends RuntimeException {
    public InvalidReservationDurationException(String message) {
        super(message);
    }
}
