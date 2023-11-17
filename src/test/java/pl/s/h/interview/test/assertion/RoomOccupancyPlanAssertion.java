package pl.s.h.interview.test.assertion;

import org.assertj.core.api.AbstractAssert;
import pl.s.h.interview.api.RoomOccupancyPlan;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class RoomOccupancyPlanAssertion extends AbstractAssert<RoomOccupancyPlanAssertion, RoomOccupancyPlan> {

    private final RoomOccupancyPlansAssertion parent;

    protected RoomOccupancyPlanAssertion(RoomOccupancyPlan actual, RoomOccupancyPlansAssertion parent) {
        super(actual, RoomOccupancyPlanAssertion.class);
        this.parent = parent;

        isNotNull();
    }

    public RoomOccupancyPlansAssertion and() {
        return parent;
    }

    public RoomOccupancyPlanAssertion withTotalEarnings(BigDecimal totalEarnings) {
        assertThat(actual.totalEarnings())
                .describedAs("Total earnings check: roomType=" + actual.roomType())
                .isEqualByComparingTo(totalEarnings);
        return this;
    }

    public RoomOccupancyPlanAssertion withRoomsOccupied(long roomsOccupied) {
        assertThat(actual.roomsOccupied())
                .describedAs("Rooms occupied check: roomType=" + actual.roomType())
                .isEqualTo(roomsOccupied);
        return this;
    }
}
