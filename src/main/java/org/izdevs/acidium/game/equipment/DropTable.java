package org.izdevs.acidium.game.equipment;

import lombok.Getter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.Percentage;
import org.izdevs.acidium.event.EntityDeathEvent;

import java.util.Map;

@Getter
public abstract class DropTable {
    /**
     * @return true if the entity should drop parameter equipment
     */
    public abstract boolean shouldDrop(EntityDeathEvent event);

    /**
     * percentage confirmed
     */
    public Map<Percentage,Equipment> dropTable;
}
