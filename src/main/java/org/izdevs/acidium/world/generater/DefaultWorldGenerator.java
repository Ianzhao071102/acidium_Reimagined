package org.izdevs.acidium.world.generater;

import com.esri.core.geometry.Point;
import org.izdevs.acidium.api.v1.Structure;
import org.izdevs.acidium.utils.RandomUtils;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.BlockType;
import org.izdevs.acidium.world.Location;
import org.izdevs.acidium.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Service
public class DefaultWorldGenerator extends WorldGenerator {
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public DefaultWorldGenerator() {
        //the new Object() is just a place holder for something important
        super("default", new Object());
    }

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
            if(!StructureHolder.structures.isEmpty()){
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
            else{
                logger.info("no structures currently registered, skipping...");
                break;
            }
        }
        //it is a lot (of combinations) huh
        world.setName(RandomUtils.getRandomString(10));
        return world;
    }

}
