package org.izdevs.acidium.game.entity.petals;

import org.izdevs.acidium.api.v1.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface PetalRepository extends CrudRepository<PetalList, User> {
}
