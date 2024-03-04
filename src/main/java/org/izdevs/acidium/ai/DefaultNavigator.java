package org.izdevs.acidium.ai;

import com.dongbat.walkable.FloatArray;
import com.dongbat.walkable.PathHelper;
import com.esri.core.geometry.Point;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.world.World;

@Getter
public class DefaultNavigator implements Navigator{

    @Setter
    PathHelper finder = new PathHelper(200, 200);

    World world = null;


    @Override
    public void init() {

    } //NOTE: THIS DOES NOTHING
    public void setWorld(World world){
        this.world = world;
        for(int i=0;i<=world.getMap().size()-1;i++){
            Point _this = world.getMap().keySet().iterator().next();
            double x = _this.getX();
            double y = _this.getY();
            finder.addRect((float) x, (float) y,1,1); //NOTE: MIGHT LOSE SOME INTEGER PARTS OF THIS THING
        }
    }

    @Override
    public FloatArray navigate(int x, int x1, int y, int y1, int hitboxRadius) {
        if(world == null) throw new NullPointerException("world to path-find does not exist...");
        FloatArray path;
        path = FloatArray.with(finder.findPath(x,y,x1,y1,hitboxRadius));
        return path;
    }
}
