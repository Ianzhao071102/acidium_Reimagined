package org.izdevs.acidium.world;

import org.springframework.stereotype.Service;

import java.util.Map;
@Service
public class WorldDataHolder {
    public static Map<World,WorldData> data;
}
