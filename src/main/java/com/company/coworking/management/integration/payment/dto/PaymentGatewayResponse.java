package com.company.coworking.management.integration.payment.dto;

import lombok.Data;

@Data
public class PaymentGatewayResponse {
    private String status;
    private String transactionId;
}
