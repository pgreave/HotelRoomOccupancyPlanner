package pl.s.h.interview.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

/**
 * Describes a room occupancy plan for specific <code>roomType</code>
 *
 * @param roomType
 * @param totalEarnings total max possible hotel earnings
 * @param roomsOccupied total max possible number of room bookings
 */
@Builder
public record RoomOccupancyPlan(

        @NotNull
        RoomType roomType,

        @NotNull
        @Min(0)
        BigDecimal totalEarnings,

        @NotNull
        @Min(0)
        long roomsOccupied) {
}
