package com.company.coworking.management.service.validation.reservation;

public final class ReservationValidationOrder {

    private ReservationValidationOrder() {
        // Private constructor to prevent instantiation
    }

    public static final int BASIC_DATA = 100;
    public static final int SPACE_EXISTS = 200;
    public static final int DATE_RANGE = 300;
    public static final int OVERLAPPING = 400;
}

