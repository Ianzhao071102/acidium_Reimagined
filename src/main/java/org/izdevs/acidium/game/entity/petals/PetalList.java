package org.izdevs.acidium.game.entity.petals;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Entity
public class PetalList {
    @OneToMany
    Set<Petal> petals = new HashSet<>();
    @Id
    @GeneratedValue
    private Long id;

    public PetalList(Petal... petals){
        this.petals.addAll(List.of(petals));
    }

    public PetalList() {}
}
