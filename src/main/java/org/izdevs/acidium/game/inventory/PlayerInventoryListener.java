package org.izdevs.acidium.game.inventory;

import jakarta.persistence.PrePersist;
import org.apache.commons.beanutils.BeanUtils;
import org.izdevs.acidium.basic.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

@Component
public class PlayerInventoryListener {
    @Autowired
    InventoryRepository inventoryRepository;

    @PrePersist
    public void prePersist(Object object) {
        PlayerInventory inv = (PlayerInventory) object;
        try {
            BeanUtils.setProperty(object, "primary", inv.primary);
            BeanUtils.setProperty(object, "armour", inv.armour);
            BeanUtils.setProperty(object, "electron", inv.electron);
            BeanUtils.setProperty(object, "crafting", null);
        } catch (InvocationTargetException | IllegalAccessException e) {
            //reset this users inventory
            inventoryRepository.deleteById(inv.owner);
            inventoryRepository.save(new PlayerInventory(inv.owner));
            throw new RuntimeException(e);
        }
    }
}
