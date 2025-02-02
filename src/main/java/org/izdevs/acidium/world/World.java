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
    /**
     * id assigned by world controller
     */
    public int index = -1;
    @NotNull String name;
    volatile AbstractMobSpawner spawner;
    boolean _spawner_on = true;

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
        if(_spawner_on){
            spawner = new SpawnerFactory().getSpawner(SpawnerType.Default);
        }
        else{
            this.spawner = new SpawnerFactory().getSpawner(SpawnerType.__EMPTY__);
        }
    }
    public World(boolean spawner_enabled) {
        this._spawner_on = spawner_enabled;
    }
}
