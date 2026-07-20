package com.company.coworking.management.util.mapper;

import com.company.coworking.management.common.mapper.GlobalMapperConfig;
import com.company.coworking.management.service.integration.payment.dto.request.PaymentValidationRequest;
import com.company.coworking.management.util.enums.PaymentMethod;
import org.mapstruct.Mapper;

import java.math.BigDecimal;

@Mapper(config = GlobalMapperConfig.class)
public interface PaymentValidationRequestMapper {

    PaymentValidationRequest toPaymentValidationRequest(Long userId, BigDecimal amount, String paymentMethod);
}
