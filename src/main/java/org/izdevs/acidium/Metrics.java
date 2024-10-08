package org.izdevs.acidium;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.MeterBinder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.networking.APIEndPoints;
import org.izdevs.acidium.networking.game.GameWSEndpoint;
import org.izdevs.acidium.tick.TickManager;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.generater.WorldController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Getter
@Slf4j
public class Metrics implements MeterBinder {
    @Autowired
    WorldController controller;

    @Autowired
    TickManager tickManager;

    @Autowired
    GameWSEndpoint endpoint;

    public Counter requests;
    public Gauge players;
    public Counter ticksElapsed;
    public Gauge ticksPerSecond;
    public Counter apiRequests;
    public Gauge entities;


    public int getEntityCount() {
        if(controller.worlds.isEmpty()) return 0;
        else {
            int _value = 0;
            for (int i = 0; i <= controller.worlds.size() - 1; i++) {
                World world = controller.worlds.get(i);

                _value += world.mobs.size();
            }
            return _value;
        }
    }

    @Override
    public void bindTo(@NonNull MeterRegistry meterRegistry) {
        log.debug("binding metrics to meter registry");
        requests = Counter.builder("requests").register(meterRegistry);

        players = Gauge.builder("players", () -> endpoint.getPlayersOnline()).register(meterRegistry);

        ticksElapsed = Counter.builder("ticks_elapsed").register(meterRegistry);

        ticksPerSecond = Gauge.builder("tps", () -> tickManager.tps).register(meterRegistry);

        apiRequests = Counter.builder("api_requests").register(meterRegistry);

        entities = Gauge.builder("entities", this::getEntityCount).register(meterRegistry);
    }
}
