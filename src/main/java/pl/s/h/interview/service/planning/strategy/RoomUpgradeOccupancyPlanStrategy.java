package pl.s.h.interview.service.planning.strategy;

import pl.s.h.interview.api.RoomType;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

public class RoomUpgradeOccupancyPlanStrategy implements RoomOccupancyPlanUpgradeStrategy {

    private static final Predicate<PlanContext> PLAN_HAS_SOME_UNBOOKED_ROOMS_PREDICATE = context -> context.getLeftOverRooms() > 0;
    private static final Predicate<PlanContext> PLAN_HAS_ANY_GUESTS_PREDICATE = context -> !context.getGuestPricesForRoom().isEmpty();

    @Override
    public List<PlanContext> apply(List<PlanContext> planContexts) {
        final Map<RoomType, PlanContext> plansByRoomType = planContexts.stream()
                .collect(Collectors.toMap(PlanContext::getRoomType, Function.identity()));

        ofNullable(plansByRoomType.get(RoomType.PREMIUM))
                .filter(PLAN_HAS_SOME_UNBOOKED_ROOMS_PREDICATE)
                .ifPresent(premiumPlan -> ofNullable(plansByRoomType.get(RoomType.ECONOMY))
                        .filter(PLAN_HAS_ANY_GUESTS_PREDICATE)
                        .filter(PLAN_HAS_SOME_UNBOOKED_ROOMS_PREDICATE.negate())
                        .ifPresent(economyPlan -> {
                            final List<BigDecimal> topPrices = findHighestPricesForPromotion(premiumPlan, economyPlan);
                            updatePremiumPlan(premiumPlan, topPrices);
                            updateEconomyPlan(economyPlan, topPrices);
                        }));

        return planContexts;
    }

    private static void updateEconomyPlan(PlanContext economyPlan, List<BigDecimal> topPrices) {
        topPrices.stream()
                .limit(economyPlan.getRoomsOccupied())
                .reduce(BigDecimal::add)
                .ifPresent(updatePlanEarningsByLosses(economyPlan));

        economyPlan.getGuestPricesForRoom().stream()
                .skip(topPrices.size())
                .limit(economyPlan.getRoomsOccupied())
                .reduce(BigDecimal::add)
                .ifPresent(updatePlanEarningsByProfits(economyPlan));
    }

    private static Consumer<BigDecimal> updatePlanEarningsByProfits(PlanContext context) {
        return earningProfits -> context.setEarnings(context.getEarnings().add(earningProfits));
    }

    private static Consumer<BigDecimal> updatePlanEarningsByLosses(PlanContext context) {
        return earningLosses -> context.setEarnings(context.getEarnings().subtract(earningLosses));
    }

    private static Consumer<BigDecimal> incrementPlanRoomOccupation(PlanContext context) {
        return unused -> context.setRoomsOccupied(context.getRoomsOccupied() + 1);
    }

    private static Consumer<BigDecimal> decrementPlanLeftOverRooms(PlanContext context) {
        return unused -> context.setLeftOverRooms(context.getLeftOverRooms() - 1);
    }

    private static List<BigDecimal> findHighestPricesForPromotion(PlanContext premiumPlan, PlanContext economyPlan) {
        return economyPlan.getGuestPricesForRoom().stream()
                .collect(Collectors.groupingBy(Function.identity(), TreeMap::new, Collectors.toList()))
                .lastEntry()
                .getValue()
                .stream()
                .limit(premiumPlan.getLeftOverRooms())
                .toList();
    }

    private static void updatePremiumPlan(PlanContext context, List<BigDecimal> topPrices) {
        topPrices.forEach(topPrice -> consumers(context).forEach(consumer -> consumer.accept(topPrice)));
    }

    private static List<Consumer<BigDecimal>> consumers(PlanContext context) {
        return List.of(
                updatePlanEarningsByProfits(context),
                incrementPlanRoomOccupation(context),
                decrementPlanLeftOverRooms(context));
    }
}
