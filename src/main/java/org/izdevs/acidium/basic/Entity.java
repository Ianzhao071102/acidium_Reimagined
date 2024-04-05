package org.izdevs.acidium.basic;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.dongbat.walkable.FloatArray;
import com.esri.core.geometry.Point2D;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.inventory.Inventory;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.WorldController;
import org.springframework.data.annotation.Id;

import java.util.Random;

import static com.esri.core.geometry.Point2D.distance;
@Setter
@Getter

public class Entity extends Resource implements Telegraph {
    Inventory inventory = new Inventory(InventoryType.Inventory);
    Inventory electronInv = new Inventory(InventoryType.Electron);
    Inventory armourInv = new Inventory(InventoryType.Armour);

    AbstractBehaviourController controller;
    @Id
    int id = 0;

    /**
        basic self-representing global state machine for a basic entity
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
        int id = random.nextInt(0,WorldController.worlds.size());
        this.world = WorldController.worlds.get(id);
        //due to issues now controller is just default controller bro come on...
        this.controller = new DefaultBehaviourController(world);
        this.id = IDGenerator.createId();
    }


    public Entity(World world,String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {

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
    }

    FloatArray pathFinderGoal = new FloatArray(2);




    @Override
    public boolean handleMessage(Telegram telegram) {
        //Let state machine handle the damn job
        return stateMachine.handleMessage(telegram);
    }

    /**
     * damages the entity one tick later
     */
    public void damage(int health){
        DelayedTask task = new DelayedTask(() -> {
            //damage the entity
            this.health -= health;
        },1,true);
        LoopManager.scheduleAsyncDelayedTask(task);
    }

    /**
     *
     * check if two entities are colliding
     */
    public static boolean isColliding(Entity a,Entity b){
        double x1,x2,y1,y2;
        x1 = a.getX();
        x2 = b.getX();
        y1 = a.getY();
        y2 = b.getY();
        //how circles work
        int addedRadius = a.getHitboxRadius() + b.getHitboxRadius();
        return distance(new Point2D(x1, y1), new Point2D(x2, y2)) < addedRadius;
    }
}
