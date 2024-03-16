package org.izdevs.acidium.basic;

import com.dongbat.walkable.FloatArray;
import com.esri.core.geometry.Point;
import jakarta.annotation.PostConstruct;
import org.izdevs.acidium.Config;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.World;
import org.springframework.scheduling.annotation.Scheduled;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DefaultBehaviourController extends AbstractBehaviourController {
    @Override
    public FloatArray nextStep() {
        if(this.controlled.getX() == 0||this.controlled.getY() == 0){
            //well uninitialized
            return null;
        }else{
            //finishes the goal
            if(this.getGoal() == this.getCurrent()){
                this.isCompleting = false;
                return null;
            }

            else{
                //requires current step of pathfinding
                AStarGridFinder<GridCell> finder = new AStarGridFinder<>(GridCell.class);
                int sizeInOneWay = world.getMap().size();
                GridCell[][] cells = new GridCell[sizeInOneWay][sizeInOneWay];

                //basically sets walkable values
                for(int i=0;i<=world.getMap().size()-1;i++){
                    Block block = world.getMap().values().iterator().next();
                    int x = block.getX();
                    int y = block.getY();

                    cells[x][y].setWalkable(block.isWalkable());
                }
                NavigationGrid<GridCell> navGrid = new NavigationGrid(cells);
                List<GridCell> path = finder.findPath((int) this.controlled.getX(),(int) this.controlled.getY(),(int) this.getGoal().get(0),(int) this.getGoal().get(1),navGrid);

                GridCell cell = path.get(0);
                FloatArray now = new FloatArray(2);
                now.set(0,cell.x);
                now.set(1,cell.y);

                return now;
            }
        }
    }

    public DefaultBehaviourController(World world,Entity entity){
        this.world = world;
    }


    @Scheduled(fixedDelay = 1000/ Config.ticksPerSecond)
    @PostConstruct
    public void move(){
        Thread thread = new Thread(){
            public void run(){
                try {
                    Thread.sleep((long) (1/getControlled().getMovementSpeed()));
                } catch (InterruptedException ignored) {
                }
                //JUST MOVE
                getControlled().setX(nextStep().get(0));
                getControlled().setY(nextStep().get(1));
            }
        };
        thread.start();
    }
}
