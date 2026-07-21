package com.company.coworking.management.pipeline.reservation.step;

import com.company.coworking.management.dto.request.CreateReservationRequest;
import com.company.coworking.management.entity.Space;
import com.company.coworking.management.enums.PaymentMethod;
import com.company.coworking.management.enums.SpaceType;
import com.company.coworking.management.pipeline.reservation.context.CreateReservationContext;
import com.company.coworking.management.service.strategy.pricing.DeskPricingStrategy;
import com.company.coworking.management.service.strategy.pricing.PricingStrategyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class CalculateReservationPriceStepTest {

    private final CalculateReservationPriceStep step = new CalculateReservationPriceStep(
            new PricingStrategyFactory(List.of(new DeskPricingStrategy()))
    );

    @Test
    void execute_ShouldCalculateFullHourlyPrice() {
        CreateReservationContext context = buildContext(
                new BigDecimal("15.00"),
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 21, 13, 0)
        );

        step.execute(context);

        Assertions.assertEquals(new BigDecimal("45.00"), context.getTotalPrice());
    }

    @Test
    void execute_ShouldRoundUpPartialHours() {
        CreateReservationContext context = buildContext(
                new BigDecimal("20.00"),
                LocalDateTime.of(2026, 7, 21, 10, 0),
                LocalDateTime.of(2026, 7, 21, 11, 30)
        );

        step.execute(context);

        Assertions.assertEquals(new BigDecimal("40.00"), context.getTotalPrice());
    }

    private CreateReservationContext buildContext(BigDecimal hourlyRate, LocalDateTime startTime, LocalDateTime endTime) {
        CreateReservationRequest request = CreateReservationRequest.builder()
                .spaceId(1L)
                .startTime(startTime)
                .endTime(endTime)
                .paymentMethod(PaymentMethod.CREDIT_CARD)
                .build();

        Space space = Space.builder()
                .id(1L)
                .type(SpaceType.DESK)
                .hourlyRate(hourlyRate)
                .build();

        CreateReservationContext context = new CreateReservationContext(request);
        context.setSpace(space);
        return context;
    }
}