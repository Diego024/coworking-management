package com.company.coworking.management.service.strategy.pricing;


import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.util.enums.SpaceType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
public class MeetingRoomPricingStrategy implements PricingStrategy {

    private static final BigDecimal SURCHARGE_MULTIPLIER = new BigDecimal("1.20");
    private static final int SURCHARGE_START_HOUR = 9;
    private static final int SURCHARGE_END_HOUR = 14;

    @Override
    public BigDecimal calculatePrice(CreateReservationContext context) {
        LocalDateTime currentHour = context.getRequest().getStartTime();
        LocalDateTime endTime = context.getRequest().getEndTime();
        BigDecimal baseRate = context.getSpace().getHourlyRate();

        BigDecimal totalPrice = BigDecimal.ZERO;

        while (currentHour.isBefore(endTime)) {
            int hourOfDay = currentHour.getHour();

            if (hourOfDay >= SURCHARGE_START_HOUR && hourOfDay < SURCHARGE_END_HOUR) {
                totalPrice = totalPrice.add(baseRate.multiply(SURCHARGE_MULTIPLIER));
            } else {
                totalPrice = totalPrice.add(baseRate);
            }

            currentHour = currentHour.plusHours(1);
        }

        return totalPrice;
    }

    @Override
    public SpaceType getSpaceType() {
        return SpaceType.MEETING_ROOM;
    }
}