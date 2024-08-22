package org.izdevs.acidium.networking.account;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OnlinePlayerRepository extends CrudRepository<JoinedPlayer,Integer> {
    JoinedPlayer findJoinedPlayerByUsername(String username);
    void deleteByUsername(String username);
}
