package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import org.izdevs.acidium.api.v1.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DefaultWorldGenerator implements WorldGenerator {
    @Override
    public World generate(long seed) {
        Map<Point, Block> blockMap = new HashMap<>();
        World world = new World(blockMap);

        Random random = new Random(seed);
        for (int i = 0; i <= 3995; i++) {
            for (int j = 0; j <= 3995; j++) {
                world.setBlockAtLoc(new Location(i, j), new Block(i, j, BlockType.VOID, true));
            }
        }

        int structureCount = random.nextInt(10, 200);
        for (int i = 0; i <= structureCount; i++) {
            Structure structure = StructureHolder.structures.get(random.nextInt(0, StructureHolder.structures.size()));
            double originX = random.nextInt(structureCount * 10 + 10, structureCount * 20 - 5);
            double originY = random.nextInt(structureCount * 10 + 10, structureCount * 20 - 5);
            Map<Point, Block> map = structure.getDescription();
            ArrayList<Point> points = new ArrayList<>(map.keySet());
            for (int i1 = 0; i1 <= structure.getDescription().size() - 1; i1++) {
                Point tp = points.get(i1);
                Point dist = new Point(originX + tp.getX(), originY + tp.getY());
                world.setBlockAtLoc(new Location((int) dist.getX(), (int) dist.getY()), map.get(tp)); //set block
            }
        }
        return world;
    }

}
