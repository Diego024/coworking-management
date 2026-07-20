package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.exception.business.PaymentDeclinedException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.integration.payment.PaymentGatewayClient;
import com.company.coworking.management.integration.payment.dto.PaymentValidationResult;
import com.company.coworking.management.integration.payment.dto.PaymentValidationRequest;
import com.company.coworking.management.mapper.PaymentValidationRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidatePaymentStep implements ReservationStep {

    private final PaymentGatewayClient paymentGatewayClient;
    private final PaymentValidationRequestMapper paymentValidationRequestMapper;

    @Override
    public void execute(CreateReservationContext context) {
        PaymentValidationRequest validationRequest = paymentValidationRequestMapper.toPaymentValidationRequest(context.getUser().getId(),
                context.getTotalPrice(),
                context.getRequest().getPaymentMethod().name());
        PaymentValidationResult result = paymentGatewayClient.processPayment(validationRequest);

        if (result == PaymentValidationResult.DECLINED) {
            throw new PaymentDeclinedException("The payment was explicitly declined by the gateway.");
        }

        context.setPaymentResult(result);
    }
}