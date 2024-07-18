package org.izdevs.acidium.game.entity.petals;


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
public class PetalService implements Ticked {
    @Autowired
    PetalNamingService service;
    @Autowired
    PetalRepository repository;

    @Autowired
    UserRepository u_repo;

    public Player initPlayerPetals(Player player) {
        String username = player.getUsername();
        User user = u_repo.findUserByUsername(username);
        Optional<PetalList> op = repository.findById(user);
        if (op.isPresent()) {
            player.setPetals(op.get().getPetals());
        } else {
            player.setPetals(new HashSet<>());
        }
        return player;
    }

    public Set<Petal> getPetals(Player player) {
        Optional<PetalList> petal = repository.findById(u_repo.findUserByUsername(player.getUsername()));
        if (petal.isPresent()) {
            return petal.get().getPetals();
        } else {
            return new HashSet<>();
        }
    }


    //todo finish this up, the petals must be ticked by id
    @Override
    public void tick() {
        //updater logic of all the petal entities
        for (int i = 0; i <= WorldController.worlds.size() - 1; i++) {
            for (int j = 0; j <= WorldController.worlds.get(i).players.size() - 1; j++) {
                Player player = WorldController.worlds.get(i).players.get(i);
                //copy inv to petals
                PlayerInventory inv = player.getInventory();
                List<Equipment> equipment = inv.armour.getItems();
                equipment.addAll(inv.electron.getItems());
                Set<Petal> tp = new HashSet<>();
                Set<Entity> petals;

                Set<Entity> finalPetals = new HashSet<>();
                Set<Petal> finalPtls = new HashSet<>();
                equipment.forEach(equipment1 -> {
                    Entity entity = null;
                    boolean e_f = false;
                    for(Entity e : player.getWorld().mobs){
                        if(e instanceof Equipment && e.getId() == equipment1.getId()){
                            e_f = true;
                            entity = equipment1;
                            break;
                        }
                    }
                    if (!e_f) {
                        entity = new Entity(player.getWorld(), player.getUsername() + service.nameObject(equipment1), equipment1.getMovementSpeed(), equipment1.getHealth(), equipment1.getHitboxRadius(), equipment1.getBDamage());
                    }
                    finalPetals.add(entity);
                    finalPtls.add(new Petal(equipment1.getMovementSpeed(), equipment1.getHealth(), equipment1.getHitboxRadius(), equipment1.getBDamage()));
                });
                petals = finalPetals;
                player.setPetals(finalPtls);


                //lock is not acquired, so re-getting player petals
                tp = player.getPetals();

                //rotate logic
                petals.forEach(petal -> {
                    //https://www.desmos.com/geometry/potzespfgj
                    Map<String, Object> attr = petal.getAttributes();
                    attr.putIfAbsent("rotate_deg", 0D);

                    //type assert is possible here...
                    double deg = (float) attr.get("rotate_deg");
                    double rad = Math.toRadians(deg);

                    int r;
                    double x, y;

                    //the distance of a petal to the player's center
                    r = 3;

                    x = r * Math.cos(rad);
                    y = r * Math.sin(rad);

                    if (deg > 180D) {
                        x = -x;
                        y = -y;
                    }

                    x += player.getX();
                    y += player.getY();

                    petal.setX(x);
                    petal.setY(y);
                });
            }
        }
    }
}
