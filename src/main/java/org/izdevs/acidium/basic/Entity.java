package org.izdevs.acidium.basic;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.dongbat.walkable.FloatArray;
import com.esri.core.geometry.Point2D;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.Basic;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.apache.naming.factory.BeanFactory;
import org.izdevs.acidium.game.inventory.Inventory;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.LoopManagerFacade;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.izdevs.acidium.utils.SpringBeanUtils;
import org.izdevs.acidium.world.PlaceHolderWorld;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.generater.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.annotation.Id;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.esri.core.geometry.Point2D.distance;

@Getter
@Setter
public class Entity extends Resource {
    AngerController controller = new DefaultAngerController();
    EntityController entityController = new BasicEntityController();
    Map<String,Object> attributes = new HashMap<>();
    boolean invincible = false;
    boolean alive = true;
    Inventory primary_inventory = new Inventory(InventoryType.Inventory);
    Inventory electronInv = new Inventory(InventoryType.Electron);
    Inventory armourInv = new Inventory(InventoryType.Armour);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id = 0;
    /**
     * basic self-representing global state machine for a basic entity
     */
    double x, y;
    String name;
    double movementSpeed;
    int health;
    int hitboxRadius;
    /**
     * body damage
     */
    int bDamage;
    Runnable attackBehaviour;
    /**
     * which world it is in
     */
    World world;
    FloatArray pathFinderGoal = new FloatArray(2);
    ScheduledTask task = new ScheduledTask(
            () -> {
                Logger logger = LoggerFactory.getLogger(this.getClass());
                if (this.getHealth() <= 0) this.alive = false;
                World current_world = this.getWorld();

                if (current_world != null) {
                    for (int i = 0; i <= current_world.mobs.size() - 1; i++) {
                        Entity mob = current_world.mobs.get(i);

                        if (isColliding(mob, this)) {
                            if (invincible) {
                                logger.debug("entity is invincible, stopped damage");
                            } else {
                                mob.setHealth(mob.getHealth() - this.getBDamage());
                                this.damage(mob.getBDamage());
                                logger.debug("entity damage is triggered, amount:" + this.getBDamage());
                            }
                        }
                    }
                }
            }
    );

    public Entity(String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super();
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;

        int id = 0;
        int size = WorldController.worlds.size();
        if(size != 0){
            id = new SecureRandom().nextInt(0,size-1);
            this.world = WorldController.worlds.get(id);
        }
        else{
            this.world = new PlaceHolderWorld("^\\S+$");
        }
    }

    public Entity(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {

        super();
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;
        this.world = world;
    }

    /**
     * check if two entities are colliding
     */
    public static boolean isColliding(Entity a, Entity b) {
        double x1, x2, y1, y2;
        x1 = a.getX();
        x2 = b.getX();
        y1 = a.getY();
        y2 = b.getY();
        //how circles work
        int addedRadius = a.getHitboxRadius() + b.getHitboxRadius();
        return distance(new Point2D(x1, y1), new Point2D(x2, y2)) < addedRadius;
    }

    //sorry but code duplicates so this is the solution...
    private static Result getMobLocation(Entity a, Entity b) {
        double x1, x2, y1, y2;
        x1 = a.getX();
        x2 = b.getX();
        y1 = a.getY();
        y2 = b.getY();
        return new Result(x1, x2, y1, y2);
    }

    /**
     * damages the entity one tick later
     */
    //could NOT happen before initialization finish
    public void damage(int health) {
        DelayedTask task = new DelayedTask(() -> {
            //damage the entity
            this.health -= health;
        }, 1, true);

        Object _mgr = SpringBeanUtils.getBean("loopManager");
        assert _mgr instanceof LoopManager;

        LoopManager manager = (LoopManager) _mgr;

        manager.scheduleAsyncDelayedTask(task);
    }

    private record Result(double x1, double x2, double y1, double y2) {
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init_finish(){
        Object _mgr = SpringBeanUtils.getBean("loopManager");
        assert _mgr instanceof LoopManager;

        LoopManager manager = (LoopManager) _mgr;

        manager.registerRepeatingTask(task);
    }
}
