package pl.s.h.interview.api;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Singular;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;

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
@Validated
public record OccupancyPlanBuildResponse(

        @NotEmpty
        @UUID
        String buildPlanId,

        @NotNull
        @Singular
        List<RoomOccupancyPlan> roomOccupancyPlans,

        @NotNull
        @PastOrPresent
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", timezone = "UTC")
        LocalDateTime planGenerationDate) {
}
