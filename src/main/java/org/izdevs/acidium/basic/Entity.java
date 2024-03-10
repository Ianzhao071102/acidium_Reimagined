package org.izdevs.acidium.basic;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
import com.badlogic.gdx.ai.msg.Telegraph;
import com.dongbat.walkable.FloatArray;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.springframework.data.annotation.Id;

@Setter
@Getter
public class Entity extends Resource implements Telegraph {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id = 0;
    StateMachine<Entity, BasicEntityState> stateMachine;
    BasicEntityState state;
    double x, y;
    String name;
    double movementSpeed;
    int health;
    int hitboxRadius;
    int bDamage;

    public Entity(String name, double movementSpeed, int health, int hitboxRadius, int bDamage) {

        super(name, false);
        this.name = name;
        this.movementSpeed = movementSpeed;
        this.health = health;
        this.hitboxRadius = hitboxRadius;
        this.bDamage = bDamage;

        //defaults state to wandering

        this.stateMachine = new DefaultStateMachine<>(this, BasicEntityState.WANDER);
    }

    FloatArray pathFinderGoal = new FloatArray(2);


    @Override
    public boolean handleMessage(Telegram telegram) {
        //Let state machine handle the damn job
        return stateMachine.handleMessage(telegram);
    }
}
