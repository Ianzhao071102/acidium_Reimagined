package org.izdevs.acidium.game.equipment;

import lombok.Getter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.Percentage;

import java.util.Map;

@Getter
public abstract class DropTable {
    /**
     * @param a the entity that is being killed
     * @param equipment the equipment that is decided to be dropped if the function returns true
     * @return true if the entity should drop parameter equipment
     */
    public abstract boolean shouldDrop(Entity a,Equipment equipment,Entity killer);

    /**
     * percentage confirmed
     */
    public Map<Percentage,Equipment> dropTable;
}
