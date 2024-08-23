package org.izdevs.acidium.networking.game.payload;
import org.izdevs.acidium.world.WarpingPoint;
import org.jetbrains.annotations.Nullable;

import lombok.Getter;

@Getter
public class WarpTeleportation {
    @Nullable
    String teleportation_code;

    WarpingPoint point;
}
