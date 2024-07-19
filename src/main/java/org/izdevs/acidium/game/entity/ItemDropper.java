package org.izdevs.acidium.game.entity;

import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.event.EntityDeathEvent;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.world.World;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ItemDropper {
    @EventListener(EntityDeathEvent.class)
    public void death(EntityDeathEvent event) {
        assert event.getSource() instanceof Entity;

        Entity entity = (Entity) event.getSource();
        entity.setAlive(false);
        entity.setHealth(0);

        if (entity instanceof Player player) {
            List<Equipment> armours = player.getInventory().armour.getItems();
            List<Equipment> inv = player.getInventory().electron.getItems();

            double x,y;
            x = player.getX();
            y = player.getY();

            //drop all equipments on the ground
            for(Equipment e:armours){
                ItemEntity i_entity = new ItemEntity(player.getWorld());
                addE(i_entity,player.getWorld());
            }
            for(Entity e:inv){
                ItemEntity i_entity = new ItemEntity(player.getWorld());
                addE(i_entity,player.getWorld());
            }
        }
    }

    public void addE(Entity e, World world){
        Map<String,Object> attr = e.getAttributes();
        attr.putIfAbsent("item",e);
        e.setAttributes(attr);

        world.mobs.add(e);
    }
}
