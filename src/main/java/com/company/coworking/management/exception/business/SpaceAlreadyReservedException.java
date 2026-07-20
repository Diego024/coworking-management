package com.company.coworking.management.exception.business;

public class SpaceAlreadyReservedException extends RuntimeException {
    public SpaceAlreadyReservedException(String message) {
        super(message);
    }
}
