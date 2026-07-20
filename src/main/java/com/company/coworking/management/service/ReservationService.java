package com.company.coworking.management.service;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.ReservationResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReservationService {
    @Transactional
    ReservationResponse createReservation(CreateReservationRequest request);

    @Transactional(readOnly = true)
    List<ReservationResponse> getCurrentUserReservations();

    @Transactional(readOnly = true)
    List<ReservationResponse> getAllReservations();

    @Transactional
    ReservationResponse cancelReservation(Long reservationId);
}
