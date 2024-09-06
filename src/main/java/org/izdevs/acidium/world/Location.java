package org.izdevs.acidium.world;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Location {
    double x, y;


    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public Location(double x,double y){
        this.x = x;
        this.y = y;
    }
}
