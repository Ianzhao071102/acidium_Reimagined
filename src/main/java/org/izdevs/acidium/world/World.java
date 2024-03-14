package org.izdevs.acidium.world;

import com.esri.core.geometry.Point;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class World {
    Map<Point,Block> map = new HashMap<>();
    public World(Map<Point,Block> map){
        this.map = map;
    }
    public Block getBlockAtLoc(Location location){
        if(map.containsKey(new Point(location.getX(),location.getY()))){
            return map.get(new Point(location.getX(),location.getY()));
        }else{
            throw new IllegalArgumentException("block is not found in:" + map);
        }
    }
    public Block setBlockAtLoc(Location location,Block block){
        if(map.containsKey(new Point(location.getX(),location.getY()))){
            return map.replace(new Point(location.getX(),location.getY()),block);
        }else{
            Point point = new Point(location.x,location.y);
            map.put(point,block);
            return block;
        }
    }
}
