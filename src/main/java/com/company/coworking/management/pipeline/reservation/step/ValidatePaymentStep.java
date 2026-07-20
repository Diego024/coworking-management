package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.exception.business.PaymentDeclinedException;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.integration.payment.PaymentGatewayClient;
import com.company.coworking.management.service.integration.payment.PaymentValidationResult;
import com.company.coworking.management.service.integration.payment.dto.request.PaymentValidationRequest;
import com.company.coworking.management.util.mapper.PaymentValidationRequestMapper;
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