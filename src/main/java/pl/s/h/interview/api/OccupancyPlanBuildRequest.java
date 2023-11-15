package pl.s.h.interview.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Singular;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Describes request for building occupancy plan
 *
 * @param planDate                    date for which the plan is requested
 * @param roomAvailabilities          configuration of available rooms
 * @param guestPriceProposalsPerNight list representing guests willingness to pay for the night at <code>planDate</code>
 */
@Builder
@Validated
public record OccupancyPlanBuildRequest(

        @NotNull
        @FutureOrPresent
        LocalDate planDate,

        @NotEmpty
        @Valid
        @Singular("roomAvailability")
        List<RoomAvailability> roomAvailabilities,

        @NotEmpty
        @Valid
        @Singular("guestPriceProposalPerNight")
        @JsonProperty("priceOptions")
        List<BigDecimal> guestPriceProposalsPerNight) {
}
