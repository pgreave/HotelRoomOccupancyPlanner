package pl.s.h.interview.service.planning;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.s.h.interview.api.OccupancyPlanBuildRequest;
import pl.s.h.interview.api.OccupancyPlanBuildResponse;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomOccupancyPlan;
import pl.s.h.interview.service.common.PlanIdGenerator;
import pl.s.h.interview.service.planning.strategy.RoomOccupancyPlanStrategy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OccupancyPlanService {

    private final PlanIdGenerator planIdGenerator;

    private final RoomOccupancyPlanStrategy occupancyPlanStrategy;

    public OccupancyPlanBuildResponse buildPlan(OccupancyPlanBuildRequest request) {
        final List<RoomAvailability> roomAvailabilities = request.roomAvailabilities();
        final List<BigDecimal> prices = request.guestPriceProposalsPerNight();

        final Collection<RoomOccupancyPlan> roomOccupancyPlans = occupancyPlanStrategy.buildPlan(roomAvailabilities, prices);

        return OccupancyPlanBuildResponse.builder()
                .buildPlanId(planIdGenerator.get())
                .roomOccupancyPlans(roomOccupancyPlans)
                .planGenerationDate(LocalDateTime.now())
                .build();
    }
}
