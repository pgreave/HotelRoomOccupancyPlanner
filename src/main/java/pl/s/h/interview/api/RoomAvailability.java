package pl.s.h.interview.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * Describes number of available rooms in a specific <code>type</code>
 *
 * @param type           room type
 * @param availableRooms number of available rooms in a specific <code>type</code>
 */
@Builder
public record RoomAvailability(

        @NotNull
        RoomType type,

        @NotNull
        @Min(0)
        long availableRooms) {
}
