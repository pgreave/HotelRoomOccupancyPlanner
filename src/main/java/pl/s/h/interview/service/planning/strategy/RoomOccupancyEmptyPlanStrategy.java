package pl.s.h.interview.service.planning.strategy;

import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomOccupancyPlan;

import java.math.BigDecimal;
import java.util.List;

public class RoomOccupancyEmptyPlanStrategy implements RoomOccupancyPlanStrategy {
    @Override
    public List<RoomOccupancyPlan> buildPlan(List<RoomAvailability> roomAvailabilities, List<BigDecimal> prices) {
        return List.of();
    }
}
