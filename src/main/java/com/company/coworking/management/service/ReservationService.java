package com.company.coworking.management.service;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse createReservation(CreateReservationRequest request);

    List<ReservationResponse> getCurrentUserReservations();

    List<ReservationResponse> getAllReservations();

    ReservationResponse cancelReservation(Long reservationId);
}
