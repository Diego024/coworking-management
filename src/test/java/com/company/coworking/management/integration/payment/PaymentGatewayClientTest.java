package com.company.coworking.management.integration.payment;

import com.company.coworking.management.integration.payment.dto.PaymentGatewayResponse;
import com.company.coworking.management.integration.payment.dto.PaymentValidationRequest;
import com.company.coworking.management.integration.payment.dto.PaymentValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentGatewayClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private PaymentProperties paymentProperties;

    @InjectMocks
    private PaymentGatewayClient paymentGatewayClient;

    @Test
    void processPayment_WhenGatewayApproves_ShouldReturnApproved() {
        PaymentValidationRequest request = buildRequest();
        PaymentGatewayResponse gatewayResponse = new PaymentGatewayResponse();
        gatewayResponse.setStatus(PaymentValidationResult.APPROVED.name());

        when(paymentProperties.getUrl()).thenReturn("http://payment-service/process");
        when(restTemplate.postForEntity(any(String.class), any(PaymentValidationRequest.class), any(Class.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(gatewayResponse));

        PaymentValidationResult result = paymentGatewayClient.processPayment(request);

        Assertions.assertEquals(PaymentValidationResult.APPROVED, result);
    }

    @Test
    void processPayment_WhenGatewayIsDown_ShouldReturnCircuitOpenFallback() {
        PaymentValidationRequest request = buildRequest();

        PaymentValidationResult result = ReflectionTestUtils.invokeMethod(
                paymentGatewayClient,
                "paymentFallback",
                request,
                new RestClientException("service down")
        );

        Assertions.assertEquals(PaymentValidationResult.PENDING_CIRCUIT_OPEN, result);
    }

    private PaymentValidationRequest buildRequest() {
        PaymentValidationRequest request = new PaymentValidationRequest();
        request.setUserId(1L);
        request.setAmount(new BigDecimal("45.00"));
        request.setPaymentMethod("CREDIT_CARD");
        return request;
    }
}