package org.izdevs.acidium.basic;

import com.dongbat.walkable.FloatArray;
import jakarta.annotation.PostConstruct;
import org.izdevs.acidium.configuration.Config;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.World;
import org.springframework.scheduling.annotation.Scheduled;
import org.xguzm.pathfinding.grid.GridCell;
import org.xguzm.pathfinding.grid.NavigationGrid;
import org.xguzm.pathfinding.grid.finders.AStarGridFinder;
import org.xguzm.pathfinding.grid.finders.GridFinderOptions;

import java.util.List;

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
                GridFinderOptions opt = new GridFinderOptions();
                opt.allowDiagonal = true;
                AStarGridFinder<GridCell> finder = new AStarGridFinder<>(GridCell.class,opt);
                int sizeInOneWay = world.getMap().size();
                GridCell[][] cells = new GridCell[sizeInOneWay][sizeInOneWay];
                NavigationGrid<GridCell> navGrid = new NavigationGrid<>(cells);
                //basically sets walkable values
                for(int i=0;i<=world.getMap().size()-1;i++){
                    Block block = world.getMap().values().iterator().next();
                    int x = block.getX();
                    int y = block.getY();

                    cells[x][y].setWalkable(block.isWalkable());
                }

                float x,y;
                List<GridCell> pathToEnd = finder.findPath(0, 0, 4, 3,navGrid);
                x = pathToEnd.get(0).x;
                y = pathToEnd.get(0).y;
                FloatArray now = new FloatArray(2);
                now.set(0,x);
                now.set(1,y);
                return now;
            }
        }
    }

    public DefaultBehaviourController(World world){
        this.world = world;
    }


    @Scheduled(fixedDelay = 1000/ Config.ticksPerSecond)
    @PostConstruct
    public void move(){
        Runnable move = new Runnable() {
            @Override
            public void run() {
                controlled.setX(nextStep().get(0));
                controlled.setY(nextStep().get(1));
            }
        };
        ScheduledTask task = new ScheduledTask(move);

        //schedule the task...
        LoopManager.scheduleAsyncDelayedTask(1,task);
    }
}
