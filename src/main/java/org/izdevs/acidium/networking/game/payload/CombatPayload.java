package org.izdevs.acidium.networking.game.payload;

import lombok.Getter;

@Getter
public class CombatPayload {
    /**
     * is the orbit extended in the next 5 ticks
     */
    CombatPositionType position_type;
}
