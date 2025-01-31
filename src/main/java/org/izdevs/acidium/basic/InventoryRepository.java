package org.izdevs.acidium.basic;

import org.izdevs.acidium.api.v1.User;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends CrudRepository<PlayerInventory, User> {
    PlayerInventory findInventoryByOwner(User user);
}
