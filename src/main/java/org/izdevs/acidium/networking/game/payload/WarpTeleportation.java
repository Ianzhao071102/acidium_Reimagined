package org.izdevs.acidium.networking.game.payload;
import org.izdevs.acidium.world.WarpingPoint;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class WarpTeleportation {
    @Nullable
    String teleportation_code;

    WarpingPoint point;
}
