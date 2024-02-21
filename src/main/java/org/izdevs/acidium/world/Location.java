package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    int x,y;
    public Location(int x,int y){
        this.x = x;
        this.y = y;
    }
}
