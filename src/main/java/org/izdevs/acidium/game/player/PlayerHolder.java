package org.izdevs.acidium.game.player;

import org.izdevs.acidium.game.entity.mechanics.PlayerEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PlayerHolder {
    public volatile Set<PlayerEntity> players = new HashSet<>();
}
