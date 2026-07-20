package com.company.coworking.management.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.integration.payment.dto.PaymentValidationRequest;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(config = GlobalMapperConfig.class)
public interface PaymentValidationRequestMapper {

    PaymentValidationRequest toPaymentValidationRequest(Long userId, BigDecimal amount, String paymentMethod);
}
