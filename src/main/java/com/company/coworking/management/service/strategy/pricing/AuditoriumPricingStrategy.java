package com.company.coworking.management.service.strategy.pricing;


import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.util.enums.SpaceType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class AuditoriumPricingStrategy implements PricingStrategy {

    private static final long DISCOUNT_THRESHOLD_HOURS = 4;
    private static final BigDecimal DISCOUNT_MULTIPLIER = new BigDecimal("0.85");

    @Override
    public BigDecimal calculatePrice(CreateReservationContext context) {
        ZonedDateTime startTime = ZonedDateTime.of(context.getRequest().getStartTime(), ZoneId.systemDefault());
        ZonedDateTime endTime = ZonedDateTime.of(context.getRequest().getEndTime(), ZoneId.systemDefault());
        BigDecimal hourlyRate = context.getSpace().getHourlyRate();

        long durationInHours = Duration.between(startTime, endTime).toHours();

        // Cobrar fracción como hora completa
        if (Duration.between(startTime, endTime).toMinutes() % 60 > 0) {
            durationInHours++;
        }

        BigDecimal totalPrice = hourlyRate.multiply(BigDecimal.valueOf(durationInHours));

        if (durationInHours >= DISCOUNT_THRESHOLD_HOURS) {
            totalPrice = totalPrice.multiply(DISCOUNT_MULTIPLIER);
        }

        return totalPrice;
    }

    @Override
    public SpaceType getSpaceType() {
        return SpaceType.AUDITORIUM;
    }
}