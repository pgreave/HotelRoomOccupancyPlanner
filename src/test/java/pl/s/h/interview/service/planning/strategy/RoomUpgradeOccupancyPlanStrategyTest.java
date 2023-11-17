package pl.s.h.interview.service.planning.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.s.h.interview.api.RoomType;
import pl.s.h.interview.test.assertion.PlanContextsAssertion;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class RoomUpgradeOccupancyPlanStrategyTest {

    private RoomUpgradeOccupancyPlanStrategy sut;

    @BeforeEach
    void setup() {
        sut = new RoomUpgradeOccupancyPlanStrategy();
    }

    @Test
    void shouldNotApplyRoomUpgradeToEconomyGuestsWhenNoPremiumRoomsAvailable() {
        // given
        Given given = new Given(createPlansForNoRoomUpgrades());

        // when & then
        Expected expectedPremiumRoom = new Expected(BigDecimal.valueOf(1054), 6, 0);
        Expected expectedEconomyRoom = new Expected(BigDecimal.valueOf(99), 1, 4);
        commonTest(given, expectedPremiumRoom, expectedEconomyRoom);
    }

    @Test
    void shouldApplyRoomUpgradeToOneTopPayingEconomyGuestWhenOnePremiumRoomAvailable() {
        // given
        Given given = new Given(createPlansForOneTopPayingEconomyGuestWhenOnePremiumRoomAvailable());

        // when & then
        Expected expectedPremiumRoom = new Expected(BigDecimal.valueOf(1153), 7, 3);
        Expected expectedEconomyRoom = new Expected(BigDecimal.valueOf(45), 1, 0);
        commonTest(given, expectedPremiumRoom, expectedEconomyRoom);
    }

    @Test
    void shouldApplyRoomUpgradeToAllTopPayingEconomyGuestsWhenEnoughPremiumRoomsAvailable() {
        // given
        Given given = new Given(createPlansForAllTopPayingEconomyGuestsWhenEnoughPremiumRoomsAvailable());

        // when & then
        Expected expectedPremiumRoom = new Expected(BigDecimal.valueOf(1252), 8, 2);
        Expected expectedEconomyRoom = new Expected(BigDecimal.valueOf(45), 1, 0);
        commonTest(given, expectedPremiumRoom, expectedEconomyRoom);
    }

    void commonTest(Given given, Expected expectedPremiumRoom, Expected expectedEconomyRoom) {
        // given
        List<PlanContext> planContexts = given.planContexts;

        // when
        final List<PlanContext> result = sut.apply(planContexts);

        // then
        assertThat(result)
                .isNotNull()
                .hasSize(2);

        PlanContextsAssertion.assertThat(result)
                .containsPlanFor(RoomType.PREMIUM)
                .withEarnings(expectedPremiumRoom.earnings())
                .withRoomsOccupied(expectedPremiumRoom.roomsOccupied())
                .withRoomsLeftOver(expectedPremiumRoom.roomsLeftOver())
                .and()
                .containsPlanFor(RoomType.ECONOMY)
                .withEarnings(expectedEconomyRoom.earnings())
                .withRoomsOccupied(expectedEconomyRoom.roomsOccupied())
                .withRoomsLeftOver(expectedEconomyRoom.roomsLeftOver());
    }

    private static List<PlanContext> createPlansForOneTopPayingEconomyGuestWhenOnePremiumRoomAvailable() {
        return List.of(
                new PlanContext(RoomType.PREMIUM, 6, BigDecimal.valueOf(1054), 4, 0, createGuestPrices(374, 209, 155, 115, 101, 100)),
                new PlanContext(RoomType.ECONOMY, 1, BigDecimal.valueOf(99), 0, 3, createGuestPrices(99, 45, 23, 22))
        );
    }

    private static List<PlanContext> createPlansForAllTopPayingEconomyGuestsWhenEnoughPremiumRoomsAvailable() {
        return List.of(
                new PlanContext(RoomType.PREMIUM, 6, BigDecimal.valueOf(1054), 4, 0, createGuestPrices(374, 209, 155, 115, 101, 100)),
                new PlanContext(RoomType.ECONOMY, 1, BigDecimal.valueOf(99), 0, 4, createGuestPrices(99, 99, 45, 23, 22))
        );
    }

    private static List<PlanContext> createPlansForNoRoomUpgrades() {
        return List.of(
                new PlanContext(RoomType.PREMIUM, 6, BigDecimal.valueOf(1054), 0, 1, createGuestPrices(374, 209, 155, 115, 101, 100)),
                new PlanContext(RoomType.ECONOMY, 1, BigDecimal.valueOf(99), 4, 4, createGuestPrices(99, 99, 45, 23, 22))
        );
    }

    private static List<BigDecimal> createGuestPrices(Integer... prices) {
        return Arrays.stream(prices)
                .map(BigDecimal::valueOf)
                .toList();
    }

    private record Given(List<PlanContext> planContexts) {
    }

    private record Expected(BigDecimal earnings, long roomsOccupied, long roomsLeftOver) {
    }
}
