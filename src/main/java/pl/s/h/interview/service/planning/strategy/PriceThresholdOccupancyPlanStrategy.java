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

    private final RoomOccupancyPlanUpgradeStrategy upgradeOccupancyPlanStrategy;

    @Override
    public List<RoomOccupancyPlan> buildPlan(List<RoomAvailability> roomAvailabilities, List<BigDecimal> prices) {
        final Map<RoomType, Long> typeToAvailabilityMap = roomAvailabilities.stream()
                .collect(Collectors.toMap(RoomAvailability::type, RoomAvailability::availableRooms));

        final List<PlanContext> planContexts = Arrays.stream(RoomType.values())
                .map(roomType -> buildPlanContext(roomType, typeToAvailabilityMap, prices))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        return postProcess(planContexts).stream()
                .map(this::build)
                .toList();
    }

    protected List<PlanContext> postProcess(List<PlanContext> planContexts) {
        if (configuration.allowUpgrades) {
            return upgradeOccupancyPlanStrategy.apply(planContexts);
        }
        return planContexts;
    }

    protected Optional<PlanContext> buildPlanContext(RoomType roomType, Map<RoomType, Long> typeToAvailabilityMap, List<BigDecimal> allPrices) {
        return Optional.ofNullable(typeToAvailabilityMap.get(roomType))
                .map(availableRooms -> {
                    final PricesSupplier pricesSupplier = buildPricesSupplier(allPrices, roomType);
                    final List<BigDecimal> pricesForRoomType = pricesSupplier.get();
                    final int numberOfGuests = pricesForRoomType.size();
                    final long roomsOccupied = calcRoomOccupancy(availableRooms, numberOfGuests);
                    final long leftOverRooms = calcEmptyRooms(availableRooms, roomsOccupied);
                    final long leftOverGuests = calcGuestsWithoutRoom(numberOfGuests, roomsOccupied);
                    final BigDecimal totalEarnings = calcTotalEarnings(pricesForRoomType, roomsOccupied);

                    return new PlanContext(roomType, roomsOccupied, totalEarnings, leftOverRooms, leftOverGuests, pricesForRoomType);
                });
    }

    protected static BigDecimal calcTotalEarnings(List<BigDecimal> pricesForRoomType, long roomsOccupied) {
        return pricesForRoomType.stream()
                .limit(roomsOccupied)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    protected static long calcGuestsWithoutRoom(int numberOfGuests, long roomsOccupied) {
        return Math.max(0, numberOfGuests - roomsOccupied);
    }

    protected static long calcEmptyRooms(Long availableRooms, long roomsOccupied) {
        return Math.max(0, (availableRooms - roomsOccupied));
    }

    protected static long calcRoomOccupancy(Long availableRooms, int availableGuests) {
        if (availableRooms > availableGuests) {
            return availableGuests;
        }
        return availableRooms;
    }

    protected PricesSupplier buildPricesSupplier(List<BigDecimal> prices, RoomType roomType) {
        final RoomThresholdConfiguration roomThresholdConfiguration = configuration.roomThresholdConfigurations.get(roomType);

        requireNonNull(roomThresholdConfiguration, "Room threshold configuration not found for room type: " + roomType);

        final BigDecimal rangeBottom = roomThresholdConfiguration.rangeBottom;
        final BigDecimal rangeTop = roomThresholdConfiguration.rangeTop;

        return Optional.ofNullable(rangeTop)
                .map(number -> PricesSupplierFactory.byRange(prices, rangeBottom, rangeTop))
                .orElse(PricesSupplierFactory.byThresholdAbove(prices, rangeBottom));
    }

    protected final RoomOccupancyPlan build(PlanContext planContext) {
        return RoomOccupancyPlan.builder()
                .roomType(planContext.getRoomType())
                .totalEarnings(planContext.getEarnings())
                .roomsOccupied(planContext.getRoomsOccupied())
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
