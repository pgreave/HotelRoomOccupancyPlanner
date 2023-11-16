package pl.s.h.interview.service.common;

import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.function.Supplier;

@Service
public class PlanIdGenerator implements Supplier<String> {
    @Override
    public String get() {
        return UUID.randomUUID().toString();
    }
}
