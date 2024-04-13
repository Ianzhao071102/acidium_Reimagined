package org.izdevs.acidium.basic;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.izdevs.acidium.api.v1.Role;
import org.izdevs.acidium.api.v1.User;
import org.springframework.data.repository.Repository;


public interface UserRepository extends Repository<User,Integer> {
    User findById(Long id);
    User findUserByRole(Role role);
}
