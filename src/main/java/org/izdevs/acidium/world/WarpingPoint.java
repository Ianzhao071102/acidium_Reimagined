package org.izdevs.acidium.world;

import com.google.gson.Gson;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class WarpingPoint {
    @Id
    @GeneratedValue
    long id;
    double x = 0xDEADBEEF,y = 0xDEADBEEF;
    double facing = 0xDEADBEEF;
    String name;
    String world_name;

    @Override
    public String toString(){
        return new Gson().toJson(this);
    }
}
