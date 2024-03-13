package org.izdevs.acidium.basic;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.dongbat.walkable.FloatArray;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.world.World;
import org.izdevs.acidium.world.WorldController;
import org.springframework.data.annotation.Id;

import java.util.Random;

@Setter
@Getter
public class Entity extends Resource implements Telegraph {
    AbstractBehaviourController controller;
    @Id
    int id = 0;
    StateMachine<Entity, BasicEntityState> stateMachine;
    BasicEntityState state;
    double x, y;
    String name;
    double movementSpeed;
    int health;
    int hitboxRadius;
    int bDamage;
    Runnable attackBehaviour;
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
        this.controller = new DefaultBehaviourController(world,this);
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
        this.controller = new DefaultBehaviourController(world,this);
    }

    FloatArray pathFinderGoal = new FloatArray(2);


    @Override
    public boolean handleMessage(Telegram telegram) {
        //Let state machine handle the damn job
        return stateMachine.handleMessage(telegram);
    }
}
