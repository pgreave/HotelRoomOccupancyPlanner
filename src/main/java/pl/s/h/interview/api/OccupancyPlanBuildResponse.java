package pl.s.h.interview.api;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Singular;
import org.hibernate.validator.constraints.UUID;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response with room occupancy plans
 *
 * @param buildPlanId        unique id of created occupancy plan
 * @param roomOccupancyPlans room occupancy plans for each {@link RoomType} value
 * @param planGenerationDate occupancy plan generation date and time
 */
@Builder
public record OccupancyPlanBuildResponse(

        @NotEmpty
        @UUID
        String buildPlanId,

        @NotNull
        @Singular
        List<RoomOccupancyPlan> roomOccupancyPlans,

        @NotNull
        @PastOrPresent
        LocalDateTime planGenerationDate) {
}
