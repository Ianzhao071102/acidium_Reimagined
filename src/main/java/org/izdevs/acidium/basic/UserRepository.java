package org.izdevs.acidium.basic;

import org.izdevs.acidium.api.v1.Role;
import org.izdevs.acidium.api.v1.User;
import org.springframework.data.repository.Repository;

public interface UserRepository extends Repository<User,Integer> {

    User findById(Long id);
    User findUserByRole(Role role);
}
