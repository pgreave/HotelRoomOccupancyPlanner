package pl.s.h.interview.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.s.h.interview.service.planning.strategy.PriceThresholdOccupancyPlanStrategy;
import pl.s.h.interview.service.planning.strategy.RoomOccupancyPlanStrategy;

@Configuration
public class PlanningStrategyConfig {

    @Bean
    @ConditionalOnProperty(prefix = "room.occupancy.plan.strategy", name = "name", havingValue = "priceThreshold")
    public RoomOccupancyPlanStrategy priceThresholdOccupancyPlanStrategy() {
        return new PriceThresholdOccupancyPlanStrategy();
    }

}
