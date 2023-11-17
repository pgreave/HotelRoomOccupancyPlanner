package pl.s.h.interview.test.assertion;

import org.assertj.core.api.AbstractAssert;
import pl.s.h.interview.api.RoomOccupancyPlan;
import pl.s.h.interview.api.RoomType;

import java.util.List;
import java.util.Optional;

public class RoomOccupancyPlansAssertion extends AbstractAssert<RoomOccupancyPlansAssertion, List<RoomOccupancyPlan>> {

    protected RoomOccupancyPlansAssertion(List<RoomOccupancyPlan> roomOccupancyPlans) {
        super(roomOccupancyPlans, RoomOccupancyPlansAssertion.class);
    }

    public static RoomOccupancyPlansAssertion assertThat(List<RoomOccupancyPlan> occupancyPlans) {
        return new RoomOccupancyPlansAssertion(occupancyPlans);
    }

    public RoomOccupancyPlanAssertion hasRoomOccupancyPlanFor(RoomType roomType) {
        final Optional<RoomOccupancyPlan> occupancyPlan = actual.stream()
                .filter(roomOccupancyPlan -> roomOccupancyPlan.roomType().equals(roomType))
                .findFirst();

        if(occupancyPlan.isEmpty()) {
            failWithMessage("Room occupancy plan list doesn't contain plan for room type: %s", roomType.name());
            return null;
        }

        return new RoomOccupancyPlanAssertion(occupancyPlan.get(), this);
    }
}
