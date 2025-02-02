package org.izdevs.acidium.game.inventory;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.PlayerInventoryConverter;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.utils.NumberUtils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;


@NoArgsConstructor
@Entity
@EntityListeners({PlayerInventoryListener.class})
public class PlayerInventory {
    //todo make inventories receive the items of a user every time class constructed
    @Id
    @GeneratedValue
    public long id;
    /**
     * has 100 slots
     */

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Getter
    @Setter
    User owner;

    public PlayerInventory(User user) {
        this.owner = user;
    }

    @Convert(converter = PlayerInventoryConverter.class)
    public Inventory primary = new Inventory(InventoryType.Inventory);
    /**
     * has 9 slots
     */
    @Convert(converter = PlayerInventoryConverter.class)
    public Inventory crafting = new Inventory(InventoryType._Crafting);
    /**
     * has 50 slots
     */
    @Convert(converter = PlayerInventoryConverter.class)
    public Inventory armour = new Inventory(InventoryType.Armour);
    /**
     * has 20 slots
     */
    @Convert(converter = PlayerInventoryConverter.class)
    public Inventory electron = new Inventory(InventoryType.Electron);

    public void setItemAtSlot(Equipment item, int slot) {
        if (NumberUtils.isInRange(0, 178, slot)) {
            if (NumberUtils.isInRange(0, 99, slot)) {
                primary.setItemAt(slot, item);
            } else if (NumberUtils.isInRange(100, 108, slot)) {
                crafting.setItemAt(slot - 100, item);
            } else if (NumberUtils.isInRange(109, 158, slot)) {
                armour.setItemAt(slot - 109, item);
            } else if (NumberUtils.isInRange(158, 178, slot)) {
                electron.setItemAt(slot - 158, item);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public Equipment getItemAtSlot(int slot) {
        if (NumberUtils.isInRange(0, 178, slot)) {
            if (NumberUtils.isInRange(0, 99, slot)) {
                return primary.getItems().get(slot);
            } else if (NumberUtils.isInRange(100, 108, slot)) {
                return crafting.getItems().get(slot - 100);
            } else if (NumberUtils.isInRange(109, 158, slot)) {
                return crafting.getItems().get(slot - 109);
            } else if (NumberUtils.isInRange(158, 178, slot)) {
                return crafting.getItems().get(slot - 158);
            }
        } else {
            throw new IllegalArgumentException();
        }
        return null;
    }
}
