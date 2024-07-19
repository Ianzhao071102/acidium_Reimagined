package org.izdevs.acidium.basic;

import com.dongbat.walkable.PathHelper;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.Location;
import org.izdevs.acidium.world.World;

import java.awt.geom.Point2D;
import java.util.*;

public class BasicEntityController implements EntityController {
    public static final int distance = 13;

    @Override
    public Entity modify(Entity snapshot) {
        Entity a = snapshot;

        //mobs can see players from 13 blocks away
        PathHelper helper = new PathHelper(distance, distance);

        //get all entity to be angry at

        long radius = (distance - 1) / 2;
        int x, y;
        x = (int) Math.round(a.x - distance);
        y = (int) Math.round(a.y - distance);
        World world = snapshot.getWorld();
        List<Map.Entry<Entity, Double>> angryAt = new ArrayList<>();
        for (int i = 0; i <= world.mobs.size() - 1; i++) {
            Entity entity = world.mobs.get(i);

            if (entity.isAlive()) {
                double dis = Point2D.distance(a.x, a.y, entity.x, entity.y);
                angryAt.add(new AbstractMap.SimpleEntry<>(entity, dis));
            }
        }

        angryAt.sort(Map.Entry.comparingByValue());
        for (int i = 1; i <= distance - 1; i++) {
            for (int j = 0; j <= distance - 1; j++) {
                int _x = x + i;
                int _y = y + j;

                Block block = world.getBlockAtLoc(new Location(_x, _y));
                if (!block.isWalkable()) {
                    helper.addRect(_x, _y, 1, 1);
                }
            }
        }
        Entity target = angryAt.get(0).getKey();
        float[] path = helper.findPath((float) a.getX(), (float) a.getY(), (float) target.x, (float) target.y, radius);
        a.setX(path[0]);
        a.setY(path[1]);
        return a;
    }
}
