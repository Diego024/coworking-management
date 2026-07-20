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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@Tag(name = "Reservations", description = "Endpoints for managing coworking space reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @Operation(
            summary = "Create a new reservation",
            description = "Creates a reservation executing validations and payment processing"
    )
    public ResponseEntity<GeneralResponse> createReservation(@Valid @RequestBody CreateReservationRequest request) {
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseBuilder.buildCreatedResponse(response);
    }

    @GetMapping
    @Operation(
            summary = "Get current user reservations",
            description = "Returns all reservations belonging to the authenticated user."
    )
    public ResponseEntity<GeneralResponse> getCurrentUserReservations() {
        List<ReservationResponse> response = reservationService.getCurrentUserReservations();
        return ResponseBuilder.buildSuccessResponse(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all reservations",
            description = "Returns all reservations. ADMIN only."
    )
    public ResponseEntity<GeneralResponse> getAllReservations() {
        List<ReservationResponse> response = reservationService.getAllReservations();
        return ResponseBuilder.buildSuccessResponse(response);
    }

    @PatchMapping("/{reservationId}/cancel")
    @Operation(
            summary = "Cancel reservation",
            description = "Cancels a reservation owned by the authenticated user. ADMIN can cancel any reservation."
    )
    public ResponseEntity<GeneralResponse> cancelReservation(@PathVariable Long reservationId) {
        ReservationResponse response = reservationService.cancelReservation(reservationId);

        return ResponseBuilder.buildSuccessResponse(response);
    }
}