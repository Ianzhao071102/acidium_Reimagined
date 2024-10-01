package org.izdevs.acidium.game.equipment;

import org.izdevs.acidium.scheduling.Ticked;
import org.springframework.stereotype.Service;

@Service
public class EquipmentTicker implements Ticked {
    @Override
    public void tick() {
        for(int i = 0;i<=EquipmentHolder.equipments.size()-1;i++){
            EquipmentHolder.equipments.forEach((a) -> {
                Equipment b = EquipmentHolder.equipments.iterator().next();
                EquipmentHolder.equipments.remove(b);
                if(b.isBroken) {
                    b.setHealth(0);
                    b.setAlive(false);

                    b.handleBroken();
                    b.handleDeath();
                }

                EquipmentHolder.equipments.add(b);
            });
        }
    }
}
