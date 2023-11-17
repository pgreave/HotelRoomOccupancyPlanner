package pl.s.h.interview.service.prices;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class PricesSupplier implements Supplier<List<BigDecimal>> {

    private final List<BigDecimal> prices;

    @Override
    public List<BigDecimal> get() {
        return prices;
    }
}
