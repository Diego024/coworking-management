package com.company.coworking.management.service.strategy.pricing;

import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.enums.SpaceType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class DeskPricingStrategy implements PricingStrategy {

    private static final long MAX_CHARGEABLE_HOURS = 6;

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

        long chargeableHours = Math.min(durationInHours, MAX_CHARGEABLE_HOURS);

        return hourlyRate.multiply(BigDecimal.valueOf(chargeableHours));
    }

    @Override
    public SpaceType getSpaceType() {
        return SpaceType.DESK;
    }
}