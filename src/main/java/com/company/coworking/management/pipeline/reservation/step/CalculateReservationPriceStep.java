package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.strategy.pricing.PricingStrategy;
import com.company.coworking.management.service.strategy.pricing.PricingStrategyFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CalculateReservationPriceStep implements ReservationStep {

    private final PricingStrategyFactory pricingStrategyFactory;

    @Override
    public void execute(CreateReservationContext context) {
        PricingStrategy strategy = pricingStrategyFactory.getStrategy(context.getSpace().getType());
        BigDecimal totalPrice = strategy.calculatePrice(context);

        context.setTotalPrice(totalPrice);
    }
}