package com.company.coworking.management.service.strategy.pricing;

import com.company.coworking.management.util.enums.SpaceType;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class PricingStrategyFactory {

    private final Map<SpaceType, PricingStrategy> strategies = new EnumMap<>(SpaceType.class);

    public PricingStrategyFactory(List<PricingStrategy> strategyList) {
        for (PricingStrategy strategy : strategyList) {
            this.strategies.put(strategy.getSpaceType(), strategy);
        }
    }

    public PricingStrategy getStrategy(SpaceType spaceType) {
        PricingStrategy strategy = strategies.get(spaceType);
        if (strategy == null) {
            throw new IllegalArgumentException("No pricing strategy found for space type: " + spaceType);
        }
        return strategy;
    }
}