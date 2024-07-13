package org.izdevs.acidium.game.entity.petals;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
public class PetalList {
    Set<Petal> petals = new HashSet<>();
    public PetalList(Petal... petals){
        this.petals.addAll(List.of(petals));
    }
}
