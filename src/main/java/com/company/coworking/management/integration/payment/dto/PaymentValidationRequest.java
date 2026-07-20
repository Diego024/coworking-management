package com.company.coworking.management.integration.payment.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentValidationRequest {
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
}
