package org.izdevs.acidium.event;

import lombok.Getter;
import org.izdevs.acidium.api.v1.Player;
import org.izdevs.acidium.game.entity.petals.Petal;
import org.springframework.context.ApplicationEvent;

@Getter
public class SwitchPetalEvent extends ApplicationEvent {
    Petal a,b;
    public SwitchPetalEvent(Petal a, Petal b, Player player) {
        super(player);

        this.a = a;
        this.b = b;
    }
}
