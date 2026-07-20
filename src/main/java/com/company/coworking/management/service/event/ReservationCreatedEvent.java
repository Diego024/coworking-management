package com.company.coworking.management.service.event;

import com.company.coworking.management.entity.Reservation;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReservationCreatedEvent {
    private final Reservation reservation;
}
