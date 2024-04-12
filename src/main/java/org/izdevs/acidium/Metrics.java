package org.izdevs.acidium;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.Getter;
import lombok.NonNull;
import org.izdevs.acidium.networking.RestAPI;
import org.izdevs.acidium.tick.TickManager;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.WorldController;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
@Getter
public class Metrics implements MeterBinder {
    public static Counter requests;
    public static Gauge players;
    public static Counter ticksElapsed;
    public static Gauge ticksPerSecond;
    public static Counter apiRequests;
    public static Gauge entities;


    public static int getEntityCount() {
        if(WorldController.worlds.isEmpty()) return 0;
        else {
            int _value = 0;
            for (int i = 0; i <= WorldController.worlds.size() - 1; i++) {
                World world = WorldController.worlds.get(i);

                _value += world.mobs.size();
            }
            return _value;
        }
    }

    @Override
    public void bindTo(@NonNull MeterRegistry meterRegistry) {
        requests = Counter.builder("requests").register(meterRegistry);

        players = Gauge.builder("players", () -> RestAPI.playersOnline).register(meterRegistry);

        ticksElapsed = Counter.builder("ticks_elapsed").register(meterRegistry);

        ticksPerSecond = Gauge.builder("tps", () -> TickManager.tps).register(meterRegistry);

        apiRequests = Counter.builder("api_requests").register(meterRegistry);

        entities = Gauge.builder("entities", Metrics::getEntityCount).register(meterRegistry);

    }
}
