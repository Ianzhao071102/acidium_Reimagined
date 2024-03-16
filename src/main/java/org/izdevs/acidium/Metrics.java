package org.izdevs.acidium;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import org.izdevs.acidium.networking.ServerHandler;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class Metrics implements MeterBinder {
    public static Counter requests;
    public static Gauge players;

    @Override
    public void bindTo(MeterRegistry meterRegistry) {
        Counter requests = Counter.builder("requests").register(meterRegistry);
        Gauge players = Gauge.builder("players", new Supplier<Number>() {
            @Override
            public Number get() {
                return ServerHandler.playersOnline;
            }
        }).register(meterRegistry);
        Metrics.requests = requests;
        Metrics.players = players;
    }
}
