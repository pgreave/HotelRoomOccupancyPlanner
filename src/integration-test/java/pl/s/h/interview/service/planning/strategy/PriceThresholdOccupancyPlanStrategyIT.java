package pl.s.h.interview.service.planning.strategy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pl.s.h.interview.BaseIT;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomOccupancyPlan;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.test.assertion.RoomOccupancyPlansAssertion;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

public class PriceThresholdOccupancyPlanStrategyIT extends BaseIT {

    @Autowired
    private PriceThresholdOccupancyPlanStrategy sut;

    @Test
    void shouldFullBookEconomyRoomsAndDoRoomUpgradeForTopPayingEconomyGuests() {
        // given
        Given given = new Given(10, 1);
        Expected expectedPremium = new Expected(BigDecimal.valueOf(1153), 7);
        Expected expectedEconomy = new Expected(BigDecimal.valueOf(45), 1);

        // when & then
        testCorrectRoomOccupancyPlansCreation(given, expectedPremium, expectedEconomy);
    }

    void testCorrectRoomOccupancyPlansCreation(Given given, Expected expectedPremium, Expected expectedEconomy) {
        // given
        List<RoomAvailability> roomAvailabilities = createRoomAvailabilities(given.premiumRooms, given.economyRooms);
        List<BigDecimal> prices = createPrices();

        // when
        final List<RoomOccupancyPlan> result = sut.buildPlan(roomAvailabilities, prices);

        //then
        RoomOccupancyPlansAssertion.assertThat(result)
                .isNotNull()
                .hasRoomOccupancyPlanFor(RoomType.ECONOMY)
                .withTotalEarnings(expectedEconomy.earnings)
                .withRoomsOccupied(expectedEconomy.roomsOccupied)
                .and()
                .hasRoomOccupancyPlanFor(RoomType.PREMIUM)
                .withTotalEarnings(expectedPremium.earnings)
                .withRoomsOccupied(expectedPremium.roomsOccupied);
    }

    private static List<RoomAvailability> createRoomAvailabilities(long premiumRooms, long economyRooms) {
        return List.of(RoomAvailability.builder()
                        .type(RoomType.PREMIUM)
                        .availableRooms(premiumRooms)
                        .build(),
                RoomAvailability.builder()
                        .type(RoomType.ECONOMY)
                        .availableRooms(economyRooms)
                        .build());
    }

    private List<BigDecimal> createPrices() {
        return Stream.of(23, 45, 155, 374, 22, 99, 100, 101, 115, 209)
                .map(BigDecimal::valueOf)
                .toList();
    }

    private record Given(long premiumRooms, long economyRooms) {
    }

    private record Expected(BigDecimal earnings, long roomsOccupied) {
    }
}
