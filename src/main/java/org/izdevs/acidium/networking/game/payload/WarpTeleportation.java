package org.izdevs.acidium.networking.game.payload;
import lombok.Getter;

@Getter
public class WarpTeleportation {
    String world_name;
    String warp_point_name;
}
