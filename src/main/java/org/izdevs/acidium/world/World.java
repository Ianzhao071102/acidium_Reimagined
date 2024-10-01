package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.spawner.AbstractMobSpawner;
import org.izdevs.acidium.game.entity.spawner.SpawnerFactory;
import org.izdevs.acidium.game.entity.spawner.SpawnerType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Setter
@Getter
public class World extends TickedWorld {
    @NotNull String name;
    volatile AbstractMobSpawner spawner;

    public Block getBlockAtLoc(Location location) {
        return WorldDataHolder.data.get(this).map.getOrDefault(new Point(location.getX(), location.getY()), null);
    }

    public void setBlockAtLoc(Location location, Block block) {
        if (WorldDataHolder.data.get(this).map.containsKey(new Point(location.getX(), location.getY()))) {
            WorldDataHolder.data.get(this).map.replace(new Point(location.getX(), location.getY()), block);
        } else {
            Point point = new Point(location.x, location.y);
            WorldDataHolder.data.get(this).map.put(point, block);
        }
    }

    public void summonEntity(Entity a, Location location) {
        a.setAlive(true);
        a.setX(location.x);
        a.setY(location.y);
        WorldDataHolder.data.get(this).entities.add(a);
    }

    @Override
    public String toString() {
        return WorldDataHolder.data.get(this).map.toString();
    }

    @PostConstruct
    public void init() {
        spawner = new SpawnerFactory().getSpawner(SpawnerType.Default);
    }
}
