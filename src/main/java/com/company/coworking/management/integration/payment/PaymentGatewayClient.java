package com.company.coworking.management.integration.payment;

import com.company.coworking.management.integration.payment.dto.PaymentValidationRequest;
import com.company.coworking.management.integration.payment.dto.PaymentGatewayResponse;
import com.company.coworking.management.integration.payment.dto.PaymentValidationResult;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Log4j2
@Component
@RequiredArgsConstructor
public class PaymentGatewayClient {

    private final RestTemplate restTemplate;
    private final PaymentProperties paymentProperties;

    private static final String PAYMENT_SERVICE = "paymentService";
    private static final String PAYMENT_FALLBACK_METHOD = "paymentFallback";

    @Retry(name = PAYMENT_SERVICE)
    @CircuitBreaker(name = PAYMENT_SERVICE, fallbackMethod = PAYMENT_FALLBACK_METHOD)
    public PaymentValidationResult processPayment(PaymentValidationRequest paymentValidationRequest) {
        log.info("Attempting to process payment of {} for user {} with {}", paymentValidationRequest.getAmount(), paymentValidationRequest.getUserId(), paymentValidationRequest.getPaymentMethod());

        ResponseEntity<PaymentGatewayResponse> response = restTemplate.postForEntity(paymentProperties.getUrl(), paymentValidationRequest, PaymentGatewayResponse.class);

        if (response.getBody() != null && PaymentValidationResult.APPROVED.name().equals(response.getBody().getStatus())) {
            log.info("Payment successful for user {}", paymentValidationRequest.getUserId());
            return PaymentValidationResult.APPROVED;
        } else {
            log.warn("Payment declined for user {}. Status code: {}", paymentValidationRequest.getUserId(), response.getStatusCode());
            return PaymentValidationResult.DECLINED;
        }
    }

    /**
     * Fallback ejecutado cuando el servicio externo falla (Timeouts, HTTP 500) o el circuito está ABIERTO.
     * En lugar de lanzar excepción, devolvemos un estado PENDING.
     */
    private PaymentValidationResult paymentFallback(PaymentValidationRequest paymentValidationRequest, Throwable t) {
        log.warn("Payment integration failed for user {}. Cause: {}", paymentValidationRequest.getUserId(), t.getMessage());
        return PaymentValidationResult.PENDING_CIRCUIT_OPEN;
    }
}
