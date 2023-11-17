package pl.s.h.interview.test.assertion;

import org.assertj.core.api.AbstractAssert;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.service.planning.strategy.PlanContext;

import java.util.List;

public class PlanContextsAssertion extends AbstractAssert<PlanContextsAssertion, List<PlanContext>> {
    protected PlanContextsAssertion(List<PlanContext> planContexts) {
        super(planContexts, PlanContextsAssertion.class);
    }

    public static PlanContextsAssertion assertThat(List<PlanContext> planContexts) {
        return new PlanContextsAssertion(planContexts);
    }

    public PlanContextAssertion containsPlanFor(RoomType roomType) {
        isNotNull();

        final List<PlanContext> foundPlanContexts = actual.stream()
                .filter(plan -> plan.getRoomType().equals(roomType))
                .toList();

        if (foundPlanContexts.isEmpty()) {
            failWithMessage("No plans found for room type: %s", roomType);
            return null;
        }

        if (foundPlanContexts.size() > 2) {
            failWithMessage("Found more that one plan for room type: %s", roomType);
            return null;
        }

        return new PlanContextAssertion(foundPlanContexts.get(0), this);
    }
}
