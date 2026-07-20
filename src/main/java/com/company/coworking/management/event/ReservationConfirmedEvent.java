package com.company.coworking.management.event;

import com.company.coworking.management.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationConfirmedEvent {
    private final Reservation reservation;
}
