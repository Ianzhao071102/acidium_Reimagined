package org.izdevs.acidium.basic;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;
import com.dongbat.walkable.FloatArray;
import org.izdevs.acidium.world.TickedWorld;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.SecureRandom;

import static java.awt.geom.Point2D.distance;

public enum BasicEntityState implements State<Entity> {
    WANDER(){
        @Override
        public void enter(Entity entity) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.debug("entity" + entity.getName() + ", entered wandering state....");
            double x = entity.getX();
            double y = entity.getY();

            //random pathfinding goal
            SecureRandom random = new SecureRandom();
            x += random.nextFloat(0,15);
            y += random.nextFloat(0,15);

            FloatArray goal = new FloatArray(2);
            goal.set(0, (float) x);
            goal.set(0, (float) y);
            entity.setPathFinderGoal(goal);
        }

        @Override
        public void update(Entity entity) {
            double mobX = entity.getX();
            double mobY = entity.getY();
            int closeX = 0;
            int closeY = 0;
            for(int i = 0; i<= TickedWorld.players.size()-1; i++){
                double x = TickedWorld.players.get(i).getX();
                double y = TickedWorld.players.get(i).getY();

                if(distance(mobX,mobY,x,y) < distance(closeX,mobY,mobX,closeY)){
                    //update close
                    closeX = (int) x;
                    closeY = (int) y;
                }
            }
            FloatArray goal = new FloatArray(2);
            goal.set(0,closeX);
            goal.set(0,closeY);
            Logger logger = LoggerFactory.getLogger(this.getClass());

            if(distance(mobX,closeY,closeX,mobY) < 15){
                entity.getStateMachine().changeState(BasicEntityState.ATTACKING);
                logger.debug("entity:" + entity.getId() + "decided to attack");
                entity.setPathFinderGoal(goal);
            }else{
                logger.debug("entity decides not to attack...");
            }
        }

        @Override
        public void exit(Entity entity) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.debug("entity" + entity.getName() + ", exited wandering state....");
        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    },

    ATTACKING(){
        @Override
        public void enter(Entity entity) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.debug("entity" + entity.getName() + ", entered attacking state....");
        }

        @Override
        public void update(Entity entity) {
            if(entity.getAttackBehaviour() == null){
                double mobX = entity.getX();
                double mobY = entity.getY();
                int closeX = 0;
                int closeY = 0;
                for(int i = 0; i<= TickedWorld.players.size()-1; i++){
                    double x = TickedWorld.players.get(i).getX();
                    double y = TickedWorld.players.get(i).getY();

                    if(distance(mobX,mobY,x,y) < distance(closeX,mobY,mobX,closeY)){
                        //update close
                        closeX = (int) x;
                        closeY = (int) y;
                    }
                }




                FloatArray goal = new FloatArray(2);
                goal.set(0,closeX);
                goal.set(1,closeY);
                entity.setPathFinderGoal(goal);
            }
        }

        @Override
        public void exit(Entity entity) {
            Logger logger = LoggerFactory.getLogger(this.getClass());
            logger.debug("entity" + entity.getName() + ", exited attacking state....");
        }

        @Override
        public boolean onMessage(Entity entity, Telegram telegram) {
            return false;
        }
    }


}
