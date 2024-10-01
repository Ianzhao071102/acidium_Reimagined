package org.izdevs.acidium.basic;


import com.dongbat.walkable.FloatArray;
import com.esri.core.geometry.Point2D;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.entity.EntityWorldAssigner;
import org.izdevs.acidium.game.inventory.Inventory;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.utils.SpringBeanUtils;
import org.izdevs.acidium.world.PlaceHolderWorld;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.generater.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.esri.core.geometry.Point2D.distance;

@Getter
@Setter
@Configurable
public class Entity extends Resource {
    @Autowired
    EntityWorldAssigner assigner;
    @Autowired
    WorldController controller;

    private boolean finished = false;
    EntityController entityController = new BasicEntityController();
    Map<String, Object> attributes = new HashMap<>();
    boolean invincible = false;
    boolean alive = true;
    Inventory primary_inventory = new Inventory(InventoryType.Inventory);
    Inventory electronInv = new Inventory(InventoryType.Electron);
    Inventory armourInv = new Inventory(InventoryType.Armour);
    double x, y;
    double movementSpeed;
    int health;
    int hitboxRadius;

    //in degrees
    int facingDir = 0;
    /**
     * body damage
     */
    int bDamage;
    Runnable attackBehaviour;
    /**
     * which world it is in
     */
    String world_name;
    private World world = null;

    public World getWorld() {
        String assigned_name = "";
        if (this.world instanceof PlaceHolderWorld) {
            assigned_name = this.assigner.assign(this);
        }
        for (World world : controller.worlds) {
            if (assigned_name.equals("__WAITING__")) {
                if (world.getName().equalsIgnoreCase(assigned_name)) {
                    return world;
                }
            }
            if (world.getName().equalsIgnoreCase(this.world_name)) {
                return world;
            }
        }
        throw new UnsupportedOperationException("world specified by entity is not found, edge case here");
    }

    FloatArray pathFinderGoal = new FloatArray(2);

    CombatPositionType position;

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

        //world should be assigned
        String result = assigner.assign(this);
        if (result.equalsIgnoreCase("__WAITING__")) {
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
    public void init_finish() {
        finished = true;
    }

    private void run_task() {
        Object _mgr = SpringBeanUtils.getBean("loopManager");
        assert _mgr instanceof LoopManager;

        LoopManager manager = (LoopManager) _mgr;

        manager.registerRepeatingTask(task);
    }

    @PostConstruct
    public void reg_task() {
        if (finished) {
            run_task();
        } else {
            //timer task
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    run_task();
                }
            };
            //init this five seconds later
            Timer timer = new Timer();
            timer.schedule(tt, 20000L);
        }
    }

    public long getId() {
        return this.id;
    }

    /**
     * handles hitbox collisions
     * can be overridden
     * @param another another entity which its hitbox is colliding with the current entity
     */
    public void handleHitboxCollision(Entity another){
        this.health -= another.getBDamage();
    }

    /**
     * handles the death of the entity
     * can be overridden
     */
    public void handleDeath(){
        this.setAlive(false);
        this.setHealth(0);
    }

    public boolean parentCheck(){
        return false;
    }
}
