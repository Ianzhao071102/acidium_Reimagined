package org.izdevs.acidium.api.v1;

import lombok.Getter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.basic.InventoryRepository;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.util.UUID;

@Getter
@Configurable(autowire = Autowire.BY_TYPE)
public class Player extends Entity {
    @Autowired
    InventoryRepository repository;

    public volatile PlayerInventory inventory;
    UUID uuid;
    User user;

    public Player(User user,double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(user.username, movementSpeed, health, hitboxRadius, bDamage);
        this.user = user;

        //create a new inventory if not found for the player
        if(repository.findInventoryByOwner(this.user) != null){
            inventory = repository.findInventoryByOwner(this.user);
        }else{
            inventory = new PlayerInventory(this.user);
            repository.save(this.inventory);
        }
    }
}
