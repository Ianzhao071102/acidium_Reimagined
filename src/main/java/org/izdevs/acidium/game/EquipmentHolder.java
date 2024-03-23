package org.izdevs.acidium.game;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

public class EquipmentHolder {
    @Getter
    @Setter
    static Set<Equipment> equipments = new HashSet<>();
    public static void register(Equipment equipment){
        equipments.add(equipment);
    }

}
