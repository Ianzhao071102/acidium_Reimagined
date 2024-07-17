package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.izdevs.acidium.game.entity.spawner.AbstractMobSpawner;
import org.izdevs.acidium.game.entity.spawner.SpawnerFactory;
import org.izdevs.acidium.game.entity.spawner.SpawnerType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@Setter
@Getter
public class World extends TickedWorld {
    @NonNull String name;
    volatile AbstractMobSpawner spawner;
    volatile Map<Point, Block> map;

    public World(Map<Point, Block> map) {
        super(map);
        this.map = map;
    }

    public Block getBlockAtLoc(Location location) {
        if (map.containsKey(new Point(location.getX(), location.getY()))) {
            return map.get(new Point(location.getX(), location.getY()));
        } else {
            throw new IllegalArgumentException("block is not found in:" + map);
        }
    }

    public void setBlockAtLoc(Location location, Block block) {
        if (map.containsKey(new Point(location.getX(), location.getY()))) {
            map.replace(new Point(location.getX(), location.getY()), block);
        } else {
            Point point = new Point(location.x, location.y);
            map.put(point, block);
        }
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    @PostConstruct
    public void init(){
        spawner = new SpawnerFactory().getSpawner(SpawnerType.Default);
    }
}
