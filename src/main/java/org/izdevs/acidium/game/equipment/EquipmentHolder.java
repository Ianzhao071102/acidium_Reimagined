package org.izdevs.acidium.game.equipment;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EquipmentHolder {
    public static volatile Set<Equipment> equipments;
}
