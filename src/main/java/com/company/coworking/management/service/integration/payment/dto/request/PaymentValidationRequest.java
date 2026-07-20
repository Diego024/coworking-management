package com.company.coworking.management.service.integration.payment.dto.request;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentValidationRequest {
    private Long userId;
    private BigDecimal amount;
    private String paymentMethod;
}
