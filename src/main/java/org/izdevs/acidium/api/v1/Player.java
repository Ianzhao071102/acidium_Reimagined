package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.basic.Entity;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingRecipeHolder;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.inventory.PlayerInventory;
import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Player extends Entity {
    volatile PlayerInventory inventory = new PlayerInventory();
    @Setter
    Entity entity;
    String username;
    UUID uuid;
    Runnable inventoryChecker = () -> {
        if (!inventory.getCrafting().get().getItems().isEmpty()) {
            CraftingRecipe recipe_ = null;
            boolean found = false;
            for (CraftingRecipe recipe : CraftingRecipeHolder.getRecipes()) {
                Set<CraftingSlot> grid = new HashSet<>();
                if (recipe.validate(new CraftingRecipe(grid))) {
                    found = true;
                    recipe_ = recipe;
                }
            }
            //if crafting recipe valid
            if (found) {
                int slot_id = 9; //(10-1)
                this.getInventory().getCrafting().get().getItems().set(slot_id,recipe_.getDestination());
            }
        }
    };

    public Player(User user, double movementSpeed, int health, int bDamage) {
        super(user.getName(), movementSpeed, health, 20, bDamage);

        UUID uuid = null;
        String username = null;
        try {
            for (int i = 0; i <= this.spec.size(); i++) {
                SpecObject object = this.spec.get(i);
                if (object.getKey().equals("uuid")) {
                    uuid = UUID.fromString((String) object.getValue());
                } else if (object.getKey().equals("username")) {
                    username = (String) object.getValue();
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e + "please contact maintainer of this class:" + Player.class + User.class + "with underlying : " + Resource.class);
        }
        this.uuid = uuid;
        this.username = username;

        LoopManager.registerRepeatingTask(new ScheduledTask(() -> this.inventoryChecker.run()));
    }

    public Player() {
        super("unset", 0, 20, 2, 0);
        this.username = "unset";
        this.uuid = UUID.randomUUID();
        LoopManager.registerRepeatingTask(new ScheduledTask(() -> this.inventoryChecker.run()));
    }

    public Player(User user, Entity entity) {
        super(user.getName(), entity.getMovementSpeed(), entity.getHealth(), entity.getHitboxRadius(), entity.getBDamage());
        UUID uuid = null;
        String username = null;
        try {
            for (int i = 0; i <= this.spec.size(); i++) {
                SpecObject object = this.spec.get(i);
                if (object.getKey().equals("uuid")) {
                    uuid = UUID.fromString((String) object.getValue());
                } else if (object.getKey().equals("username")) {
                    username = (String) object.getValue();
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e + "please contact maintainer of this class:" + Player.class + User.class + "with underlying : " + Resource.class);
        }
        this.uuid = uuid;
        this.username = username;
        this.entity = entity;
        LoopManager.registerRepeatingTask(new ScheduledTask(() -> this.inventoryChecker.run()));
    }
}
