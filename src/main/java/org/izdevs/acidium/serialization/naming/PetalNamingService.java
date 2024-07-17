package org.izdevs.acidium.serialization.naming;

import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.entity.petals.Petal;
import org.springframework.stereotype.Service;

@Service
public class PetalNamingService implements NameGenerator{

    @Override
    public String nameObject(Object object) {
        assert object instanceof Entity;
        Entity petal = (Entity) object;

        return "__Petal_" + petal.getName();
    }
}
