package pl.s.h.interview.service.planning.strategy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;
import pl.s.h.interview.api.RoomType;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@Validated
public class Plan {
    @NotNull
    private RoomType roomType;
    @Min(0)
    private long roomsOccupied;
    @NotNull
    private BigDecimal earnings;
    @Min(0)
    private long leftOverRooms;
    @Min(0)
    private long leftOverGuests;
    @NotNull
    private List<BigDecimal> guestPricesForRoom;
}
