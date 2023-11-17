package pl.s.h.interview.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.service.planning.strategy.PriceThresholdOccupancyPlanStrategy;
import pl.s.h.interview.service.planning.strategy.PriceThresholdOccupancyPlanStrategy.PriceThresholdOccupancyPlanStrategyConfiguration;
import pl.s.h.interview.service.planning.strategy.PriceThresholdOccupancyPlanStrategy.RoomThresholdConfiguration;
import pl.s.h.interview.service.planning.strategy.RoomOccupancyPlanStrategy;
import pl.s.h.interview.service.planning.strategy.RoomOccupancyPlanUpgradeStrategy;
import pl.s.h.interview.service.planning.strategy.RoomUpgradeOccupancyPlanStrategy;

import java.math.BigDecimal;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "room.occupancy.plan.strategy", name = "name", havingValue = "priceThreshold")
public class PlanningStrategyConfig {

    public static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100.00);

    @Bean
    public RoomOccupancyPlanStrategy priceThresholdOccupancyPlanStrategy(PriceThresholdOccupancyPlanStrategyConfiguration configuration,
                                                                         RoomOccupancyPlanUpgradeStrategy roomOccupancyPlanUpgradeStrategy) {
        return new PriceThresholdOccupancyPlanStrategy(configuration, roomOccupancyPlanUpgradeStrategy);
    }

    @Bean
    public RoomOccupancyPlanUpgradeStrategy roomOccupancyPlanUpgradeStrategy() {
        return new RoomUpgradeOccupancyPlanStrategy();
    }

    @Bean
    public PriceThresholdOccupancyPlanStrategyConfiguration priceThresholdOccupancyPlanStrategyConfiguration() {
        return new PriceThresholdOccupancyPlanStrategyConfiguration(
                Map.of(
                        RoomType.PREMIUM, new RoomThresholdConfiguration(ONE_HUNDRED, null),
                        RoomType.ECONOMY, new RoomThresholdConfiguration(BigDecimal.ZERO, ONE_HUNDRED)),
                true
        );
    }
}
