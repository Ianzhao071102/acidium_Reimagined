package org.izdevs.acidium.security;

import org.izdevs.acidium.api.v1.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface RoleRepository extends CrudRepository<Role,Long> {
    Role findById(long id);
    Role findByLevelsGranted(Set<Role.Level> levelsGranted);
}
