package com.company.coworking.management.service.integration.payment.dto.response;

import lombok.Data;

@Data
public class PaymentGatewayResponse {
    private String status;
    private String transactionId;
}
