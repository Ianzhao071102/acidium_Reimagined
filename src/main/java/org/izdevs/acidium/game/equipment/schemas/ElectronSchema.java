package org.izdevs.acidium.game.equipment.schemas;

import org.izdevs.acidium.game.equipment.EquipmentSchema;
import org.izdevs.acidium.game.inventory.InventoryType;

import java.util.Set;

public class ElectronSchema extends EquipmentSchema {

    public ElectronSchema() {
        super(200, Set.of(InventoryType.Electron), "Electron");
    }
}
