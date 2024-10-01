package org.izdevs.acidium.api.v1;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingRecipeHolder;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.entity.petals.Petal;
import org.izdevs.acidium.game.entity.petals.PetalService;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.izdevs.acidium.networking.game.payload.CombatPositionType;
import org.izdevs.acidium.scheduling.DelayedTask;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.utils.SpringBeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Player extends Entity {
    public volatile PlayerInventory inventory = new PlayerInventory();
    String username;
    UUID uuid;

    public Player(String username,double movementSpeed, int health, int hitboxRadius, int bDamage) {
        super(username, movementSpeed, health, hitboxRadius, bDamage);
    }
}
