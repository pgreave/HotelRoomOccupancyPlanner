package pl.s.h.interview.service.prices;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class PricesSupplierFactory {

    private static final Comparator<BigDecimal> DESCENDING_COMPARATOR = Comparator.reverseOrder();

    public static PricesSupplier byPredicate(Collection<BigDecimal> prices, Predicate<BigDecimal> predicate) {
        final List<BigDecimal> filtered = prices.stream().filter(predicate).sorted(DESCENDING_COMPARATOR).toList();
        return new PricesSupplier(filtered);
    }

    public static PricesSupplier byThresholdAbove(Collection<BigDecimal> prices, BigDecimal thresholdInclusive) {
        final Predicate<BigDecimal> aboveThresholdPredicate = aboveThresholdPredicate(thresholdInclusive);
        return byPredicate(prices, aboveThresholdPredicate);
    }

    public static PricesSupplier byRange(Collection<BigDecimal> prices, BigDecimal minRangeInclusive, BigDecimal maxRangeExclusive) {
        final Predicate<BigDecimal> aboveThresholdPredicate = aboveThresholdPredicate(minRangeInclusive);
        final Predicate<BigDecimal> belowThresholdPredicate = belowThresholdPredicate(maxRangeExclusive);
        final Predicate<BigDecimal> rangePredicate = aboveThresholdPredicate.and(belowThresholdPredicate);

        return byPredicate(prices, rangePredicate);
    }

    private static Predicate<BigDecimal> belowThresholdPredicate(BigDecimal maxRangeExclusive) {
        return bigDecimal -> bigDecimal.compareTo(maxRangeExclusive) < 0;
    }

    private static Predicate<BigDecimal> aboveThresholdPredicate(BigDecimal minRangeInclusive) {
        return bigDecimal -> bigDecimal.compareTo(minRangeInclusive) >= 0;
    }
}
