package com.company.coworking.management.pipeline.reservation.context;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.entity.Reservation;
import com.company.coworking.management.entity.Space;
import com.company.coworking.management.entity.User;
import com.company.coworking.management.service.integration.payment.PaymentValidationResult;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateReservationContext {

    private final CreateReservationRequest request;

    private PaymentValidationResult paymentResult;

    private User user;

    private Space space;

    private BigDecimal totalPrice;

    private Reservation reservation;

    public CreateReservationContext(CreateReservationRequest request) {
        this.request = request;
    }
}