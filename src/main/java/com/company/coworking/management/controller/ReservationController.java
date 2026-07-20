package com.company.coworking.management.controller;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.common.response.GeneralResponse;
import com.company.coworking.management.dto.response.ReservationResponse;
import com.company.coworking.management.common.response.ResponseBuilder;
import com.company.coworking.management.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<GeneralResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseBuilder.buildCreatedResponse(response);
    }

    @GetMapping
    public ResponseEntity<GeneralResponse> getCurrentUserReservations() {
        List<ReservationResponse> response = reservationService.getCurrentUserReservations();
        return ResponseBuilder.buildSuccessResponse(response);
    }

    @GetMapping("/all")
    public ResponseEntity<GeneralResponse> getAllReservations() {
        List<ReservationResponse> response = reservationService.getAllReservations();
        return ResponseBuilder.buildSuccessResponse(response);
    }

    @PatchMapping("/{reservationId}/cancel")
    public ResponseEntity<GeneralResponse> cancelReservation(@PathVariable Long reservationId) {
        ReservationResponse response = reservationService.cancelReservation(reservationId);

        return ResponseBuilder.buildSuccessResponse(response);
    }
}