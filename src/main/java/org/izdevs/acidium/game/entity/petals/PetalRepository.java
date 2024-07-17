package org.izdevs.acidium.game.entity.petals;

import org.izdevs.acidium.api.v1.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface PetalRepository extends CrudRepository<PetalList, User> {
}
