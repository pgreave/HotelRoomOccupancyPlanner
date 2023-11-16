package pl.s.h.interview.service.planning.strategy;

import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomOccupancyPlan;

import java.math.BigDecimal;
import java.util.List;

public interface RoomOccupancyPlanStrategy {

    List<RoomOccupancyPlan> buildPlan(final List<RoomAvailability> roomAvailabilities, final List<BigDecimal> prices);
}
