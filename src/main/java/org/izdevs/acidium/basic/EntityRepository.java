package org.izdevs.acidium.basic;

import org.springframework.data.repository.Repository;

public interface EntityRepository extends Repository<Entity,Integer> {

    Entity findById(int id);

}
