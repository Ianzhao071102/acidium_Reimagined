package org.izdevs.acidium.game;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.API;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class EquipmentHolder {
    @Getter
    @Setter
    static Set<Equipment> equipments = new HashSet<>();
    public static void register(Equipment equipment){
        equipments.add(equipment);
    }

    public static void init() throws InstantiationException, IllegalAccessException {
        Logger logger = LoggerFactory.getLogger(EquipmentHolder.class);
        Reflections reflections = new Reflections("org.izdevs.acidium.game");

        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(Equipment.class).asClass());
        for (Class<?> subType : subTypes) {
            logger.debug(subType.newInstance().toString());
        }
    }
}
