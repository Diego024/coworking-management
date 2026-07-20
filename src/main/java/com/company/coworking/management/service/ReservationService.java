package com.company.coworking.management.service;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.ReservationResponse;
import org.springframework.transaction.annotation.Transactional;

public interface ReservationService {
    @Transactional
    ReservationResponse createReservation(CreateReservationRequest request);
}
