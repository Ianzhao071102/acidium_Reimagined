package org.izdevs.acidium.basic;

import org.izdevs.acidium.api.v1.Role;
import org.izdevs.acidium.api.v1.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findById(Long id);
    User findByRole(Role role);
    User findByUsername(String username);

    User findUsersByUsernameContainingIgnoreCase(String param);

    void deleteUserByUsername(String username);
    boolean existsByUsername(String username);
}
