package com.company.coworking.management.service.strategy.pricing;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.util.enums.SpaceType;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(CreateReservationContext context);

    SpaceType getSpaceType();
}