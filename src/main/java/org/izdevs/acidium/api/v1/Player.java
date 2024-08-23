package org.izdevs.acidium.api.v1;

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
import org.izdevs.acidium.utils.SpringBeanUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class Player extends Entity {

    public volatile PlayerInventory inventory = new PlayerInventory();
    @Setter
    //todo remove this field, migrate all reference to the Player.java class itself
    Entity entity;
    String username;
    UUID uuid;
    Runnable inventoryChecker = () -> {
        if (!inventory.crafting.getItems().isEmpty()) {
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
                this.getInventory().crafting.getItems().set(slot_id,recipe_.getDestination());
            }
        }
    };

    @Setter
    @Getter
    volatile Set<Petal> petals = new HashSet<>();

    public Player() {
        super("unset", 0, 20, 2, 0);
        this.username = "unset";
        this.uuid = UUID.randomUUID();
    }

    public Player(User user, Entity entity) {
        super(user.getUsername(), entity.getMovementSpeed(), entity.getHealth(), entity.getHitboxRadius(), entity.getBDamage());
        this.uuid = user.uuid;
        this.username = user.username;
        this.entity = entity;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init_finish(){
        Object _mgr = SpringBeanUtils.getBean("loopManager");
        assert _mgr instanceof LoopManager;
        Object _petal = SpringBeanUtils.getBean("petalService");
        assert _petal instanceof PetalService;

        LoopManager manager = (LoopManager) _mgr;
        PetalService svc = (PetalService) _petal;
        manager.scheduleAsyncDelayedTask(new DelayedTask(() -> {
            this.petals = ((PetalService) _petal).getPetals(this);
        },1));
        manager.registerRepeatingTask(new ScheduledTask(() -> this.inventoryChecker.run()));
    }
}
