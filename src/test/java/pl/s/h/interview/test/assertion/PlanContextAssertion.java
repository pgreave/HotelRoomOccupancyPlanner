package pl.s.h.interview.test.assertion;

import org.assertj.core.api.AbstractAssert;
import pl.s.h.interview.service.planning.strategy.PlanContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PlanContextAssertion extends AbstractAssert<PlanContextAssertion, PlanContext> {

    private final PlanContextsAssertion parent;

    public PlanContextAssertion(PlanContext planContext, PlanContextsAssertion parent) {
        super(planContext, PlanContextAssertion.class);
        this.parent = parent;

        isNotNull();
    }

    public PlanContextAssertion withRoomsOccupied(long roomsOccupied) {
        assertThat(actual.getRoomsOccupied())
                .describedAs("Rooms occupied check for room type: %s", actual.getRoomType())
                .isEqualTo(roomsOccupied);
        return this;
    }

    public PlanContextAssertion withEarnings(BigDecimal earnings) {
        assertThat(actual.getEarnings())
                .describedAs("Earnings check for room type: %s", actual.getRoomType())
                .isEqualByComparingTo(earnings);
        return this;
    }

    public PlanContextAssertion withRoomsLeftOver(long roomsLeftOver) {
        assertThat(actual.getLeftOverRooms())
                .describedAs("Rooms left over check for room type: %s", actual.getRoomType())
                .isEqualTo(roomsLeftOver);
        return this;
    }

    public PlanContextsAssertion and() {
        return parent;
    }
}
