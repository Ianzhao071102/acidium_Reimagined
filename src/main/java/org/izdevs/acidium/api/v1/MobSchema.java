package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.game.equipment.DropTable;
import org.izdevs.acidium.game.equipment.EquipmentSchema;
import org.izdevs.acidium.serialization.annotations.ResourceSchemaDefinition;
import org.springframework.stereotype.Component;

import java.util.Set;
@ResourceSchemaDefinition("Mob")
@Component
@Getter
@Setter
public class MobSchema {
    String mobName;
    DropTable dropTable;
    Set<EquipmentSchema> equipments;
    double speed;
    int health;
    int hitboxRadius;
    int bDamage; //body damage
}
