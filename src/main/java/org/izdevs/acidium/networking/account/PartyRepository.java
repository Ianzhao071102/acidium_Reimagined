package org.izdevs.acidium.networking.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

//set of usernames mapped to the party name
@Repository
public interface PartyRepository extends CrudRepository<Set<String>,String> {
}
