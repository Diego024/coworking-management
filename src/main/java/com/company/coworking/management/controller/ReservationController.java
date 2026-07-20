package com.company.coworking.management.controller;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.dto.response.GeneralResponse;
import com.company.coworking.management.dto.response.ReservationResponse;
import com.company.coworking.management.dto.response.ResponseBuilder;
import com.company.coworking.management.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Endpoints for managing coworking space reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(summary = "Create a new reservation", description = "Creates a reservation executing validations and payment processing")
    public ResponseEntity<GeneralResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseBuilder.buildCreatedResponse(response);
    }
}