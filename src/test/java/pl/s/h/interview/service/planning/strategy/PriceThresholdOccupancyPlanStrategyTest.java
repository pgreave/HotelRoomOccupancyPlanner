package pl.s.h.interview.service.planning.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import pl.s.h.interview.api.RoomAvailability;
import pl.s.h.interview.api.RoomOccupancyPlan;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.test.assertion.RoomOccupancyPlansAssertion;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

class PriceThresholdOccupancyPlanStrategyTest {

    static final BigDecimal ONE_HUNDRED = BigDecimal.valueOf(100.00);

    private PriceThresholdOccupancyPlanStrategy.PriceThresholdOccupancyPlanStrategyConfiguration configuration;

    private PriceThresholdOccupancyPlanStrategy sut;

    @BeforeEach
    public void setup() {
        configuration = createConfig(true);
        sut = new PriceThresholdOccupancyPlanStrategy(configuration);
    }

    @Test
    void shouldCalculateFullRoomOccupancyForTopPayingGuests() {
        // given
        Given given = new Given(3, 3);
        Expected expectedPremium = new Expected(BigDecimal.valueOf(738), 3);
        Expected expectedEconomy = new Expected(BigDecimal.valueOf(167), 3);

        // when & then
        testCorrectRoomOccupancyPlansCreation(given, expectedPremium, expectedEconomy);
    }

    @Test
    void shouldBookAllGuestsAndLeaveEmptyRoomsWhenAvailableRoomsIsMoreThenGuests() {
        // given
        Given given = new Given(7, 5);
        Expected expectedPremium = new Expected(BigDecimal.valueOf(1054), 6);
        Expected expectedEconomy = new Expected(BigDecimal.valueOf(189), 4);

        // when & then
        testCorrectRoomOccupancyPlansCreation(given, expectedPremium, expectedEconomy);
    }

    @Test
    void shouldFullBookPremiumRoomsForPremiumGuestsAndSomeEconomyRoomsForAllEconomyGuests() {
        // given
        Given given = new Given(2, 7);
        Expected expectedPremium = new Expected(BigDecimal.valueOf(583), 2);
        Expected expectedEconomy = new Expected(BigDecimal.valueOf(189), 4);

        // when & then
        testCorrectRoomOccupancyPlansCreation(given, expectedPremium, expectedEconomy);
    }

    @Test
    @Disabled
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

    private PriceThresholdOccupancyPlanStrategy.PriceThresholdOccupancyPlanStrategyConfiguration createConfig(boolean allowRoomUpgrades) {
        return new PriceThresholdOccupancyPlanStrategy.PriceThresholdOccupancyPlanStrategyConfiguration(
                Map.of(
                        RoomType.PREMIUM, new PriceThresholdOccupancyPlanStrategy.RoomThresholdConfiguration(ONE_HUNDRED, null),
                        RoomType.ECONOMY, new PriceThresholdOccupancyPlanStrategy.RoomThresholdConfiguration(BigDecimal.ZERO, ONE_HUNDRED)),
                allowRoomUpgrades
        );
    }

    static class Given {
        final long premiumRooms;
        final long economyRooms;

        Given(long premiumRooms, long economyRooms) {
            this.premiumRooms = premiumRooms;
            this.economyRooms = economyRooms;
        }
    }

    static class Expected {
        final BigDecimal earnings;
        final long roomsOccupied;

        Expected(BigDecimal earnings, long roomsOccupied) {
            this.earnings = earnings;
            this.roomsOccupied = roomsOccupied;
        }
    }
}
