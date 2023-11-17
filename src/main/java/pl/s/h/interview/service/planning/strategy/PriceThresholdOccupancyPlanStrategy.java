package pl.s.h.interview.service.planning.strategy;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomOccupancyPlan;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.service.prices.PricesSupplier;
import pl.s.h.interview.service.prices.PricesSupplierFactory;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
public class PriceThresholdOccupancyPlanStrategy implements RoomOccupancyPlanStrategy {

    private final PriceThresholdOccupancyPlanStrategyConfiguration configuration;

    @Override
    public List<RoomOccupancyPlan> buildPlan(List<RoomAvailability> roomAvailabilities, List<BigDecimal> prices) {
        final Map<RoomType, Long> typeToAvailabilityMap = roomAvailabilities.stream()
                .collect(Collectors.toMap(RoomAvailability::type, RoomAvailability::availableRooms));

        return Arrays.stream(RoomType.values())
                .map(roomType -> buildRoomOccupancyPlan(roomType, typeToAvailabilityMap, prices))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(this::build)
                .toList();
    }

    private Optional<Plan> buildRoomOccupancyPlan(RoomType roomType, Map<RoomType, Long> typeToAvailabilityMap, List<BigDecimal> allPrices) {
        return Optional.ofNullable(typeToAvailabilityMap.get(roomType))
                .map(availableRooms -> {
                    final PricesSupplier pricesSupplier = buildPricesSupplier(allPrices, roomType);
                    final List<BigDecimal> pricesForRoomType = pricesSupplier.get();
                    final int numberOfGuests = pricesForRoomType.size();
                    final long roomsOccupied = calcRoomOccupancy(availableRooms, numberOfGuests);
                    final long leftOverRooms = calcEmptyRooms(availableRooms, roomsOccupied);
                    final long leftOverGuests = calcGuestsWithoutRoom(numberOfGuests, roomsOccupied);

                    final BigDecimal totalEarnings = pricesForRoomType.stream()
                            .limit(roomsOccupied)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new Plan(roomType, roomsOccupied, totalEarnings, leftOverRooms, leftOverGuests, pricesForRoomType);
                });
    }

    private static long calcGuestsWithoutRoom(int numberOfGuests, long roomsOccupied) {
        return Math.max(0, numberOfGuests - roomsOccupied);
    }

    private static long calcEmptyRooms(Long availableRooms, long roomsOccupied) {
        return Math.max(0, (availableRooms - roomsOccupied));
    }

    private static long calcRoomOccupancy(Long availableRooms, int availableGuests) {
        if (availableRooms > availableGuests) {
            return availableGuests;
        }
        return availableRooms;
    }

    private PricesSupplier buildPricesSupplier(List<BigDecimal> prices, RoomType roomType) {
        final RoomThresholdConfiguration roomThresholdConfiguration = configuration.roomThresholdConfigurations.get(roomType);

        requireNonNull(roomThresholdConfiguration, "Room threshold configuration not found for room type: " + roomType);

        final BigDecimal rangeBottom = roomThresholdConfiguration.rangeBottom;
        final BigDecimal rangeTop = roomThresholdConfiguration.rangeTop;

        return Optional.ofNullable(rangeTop)
                .map(number -> PricesSupplierFactory.byRange(prices, rangeBottom, rangeTop))
                .orElse(PricesSupplierFactory.byThresholdAbove(prices, rangeBottom));
    }

    private RoomOccupancyPlan build(Plan plan) {
        return RoomOccupancyPlan.builder()
                .roomType(plan.getRoomType())
                .totalEarnings(plan.getEarnings())
                .roomsOccupied(plan.getRoomsOccupied())
                .build();
    }

    @RequiredArgsConstructor
    public static class PriceThresholdOccupancyPlanStrategyConfiguration {
        @NotEmpty
        private final Map<RoomType, RoomThresholdConfiguration> roomThresholdConfigurations;
        @NotNull
        private final Boolean allowUpgrades;
    }

    @RequiredArgsConstructor
    public static class RoomThresholdConfiguration {
        private final BigDecimal rangeBottom;
        private final BigDecimal rangeTop;
    }
}
