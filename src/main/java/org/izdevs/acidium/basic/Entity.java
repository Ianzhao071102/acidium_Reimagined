package org.izdevs.acidium.basic;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.dongbat.walkable.FloatArray;
import com.esri.core.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.game.inventory.Inventory;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.generater.WorldController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;

import java.util.Random;

import static com.esri.core.geometry.Point2D.distance;

@Setter
@Getter

public class Entity extends Resource implements Telegraph {
    boolean invincible = false;
    boolean alive = true;
    Inventory primary_inventory = new Inventory(InventoryType.Inventory);
    Inventory electronInv = new Inventory(InventoryType.Electron);
    Inventory armourInv = new Inventory(InventoryType.Armour);

    AbstractBehaviourController controller;
    @Id
    int id = 0;

    /**
     * basic self-representing global state machine for a basic entity
     */
    StateMachine<Entity, BasicEntityState> stateMachine;
    BasicEntityState state;
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
                        Mob mob = current_world.mobs.get(i);

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

        super(name, false);
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;

        //defaults state to wandering
        this.stateMachine = new DefaultStateMachine<>(this, BasicEntityState.WANDER);

        Random random = new Random();
        int id = random.nextInt(0, WorldController.worlds.size());
        this.world = WorldController.worlds.get(id);
        //due to issues now controller is just default controller bro come on...
        this.controller = new DefaultBehaviourController(world);
        this.id = IDGenerator.createId();
        init();
    }


    public Entity(World world, String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {

        super(name, false);
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;

        //defaults state to wandering
        this.stateMachine = new DefaultStateMachine<>(this, BasicEntityState.WANDER);

        this.world = world;
        this.controller = new DefaultBehaviourController(world);
        this.id = IDGenerator.createId();
        init();
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

    public static boolean isColliding(Mob a, Entity b) {
        final Result result = getMobLocation(a, b);
        //how circles work
        int addedRadius = a.getHitboxRadius() + b.getHitboxRadius();
        return distance(new Point2D(result.x1(), result.y1()), new Point2D(result.x2(), result.y2())) < addedRadius;
    }

    //sorry but code duplicates so this is the solution...
    private static Result getMobLocation(Mob a, Entity b) {
        double x1, x2, y1, y2;
        x1 = a.getX();
        x2 = b.getX();
        y1 = a.getY();
        y2 = b.getY();
        return new Result(x1, x2, y1, y2);
    }

    @Override
    public boolean handleMessage(Telegram telegram) {
        //Let state machine handle the damn job
        return stateMachine.handleMessage(telegram);
    }

    /**
     * damages the entity one tick later
     */
    public void damage(int health) {
        DelayedTask task = new DelayedTask(() -> {
            //damage the entity
            this.health -= health;
        }, 1, true);
        LoopManager.scheduleAsyncDelayedTask(task);
    }

    private void init() {
        LoopManager.registerRepeatingTask(task);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.debug("entity created and finished initializing...");
    }

    private record Result(double x1, double x2, double y1, double y2) {
    }
}
