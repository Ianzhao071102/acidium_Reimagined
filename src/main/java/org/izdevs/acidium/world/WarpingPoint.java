package org.izdevs.acidium.world;

import com.google.gson.Gson;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Entity
public class WarpingPoint {
    double x = 0xDEADBEEF,y = 0xDEADBEEF;
    double facing = 0xDEADBEEF;
    String name;
    String world_name;

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
