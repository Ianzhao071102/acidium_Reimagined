package org.izdevs.acidium.game.equipment;

import lombok.Getter;
import lombok.Setter;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@Getter
@Service
public class EquipmentHolder {
    @Autowired
    Set<Equipment> equipments;
}
