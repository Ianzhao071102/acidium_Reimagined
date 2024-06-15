package org.izdevs.acidium.scheduling;

import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class AcidThreadFactory implements ThreadFactory {
    private final AtomicInteger threadCounter = new AtomicInteger();

    @Override
    public Thread newThread(@NotNull Runnable runnable) {
        return new Thread(runnable, "Acidium-scheduler-" + threadCounter.getAndIncrement());
    }
}
