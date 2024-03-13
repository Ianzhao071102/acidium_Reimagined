package org.izdevs.acidium.basic;

import com.dongbat.walkable.FloatArray;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.world.World;

@Getter
public abstract class AbstractBehaviourController {

    //bro get only
    Entity controlled;

    @Setter
    FloatArray goal;

    abstract public FloatArray nextStep();

    @Setter
    World world;

    FloatArray current;

    boolean isCompleting = true;
}
