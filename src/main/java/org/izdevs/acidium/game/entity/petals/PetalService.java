package org.izdevs.acidium.game.entity.petals;


import org.checkerframework.checker.units.qual.A;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.UserRepository;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.izdevs.acidium.scheduling.Ticked;
import org.izdevs.acidium.serialization.naming.PetalNamingService;
import org.izdevs.acidium.world.generater.WorldController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("petalService")
public class PetalService {
    @Autowired
    PetalNamingService service;
    @Autowired
    PetalRepository repository;

    @Autowired
    UserRepository u_repo;
    @Autowired
    WorldController controller;

    public Set<Petal> getPetals(Player player) {
        Optional<PetalList> petal = repository.findById(u_repo.findByUsername(player.getUsername()));
        if (petal.isPresent()) {
            return petal.get().getPetals();
        } else {
            return new HashSet<>();
        }
    }
}
