package org.izdevs.acidium.security;

import org.izdevs.acidium.api.v1.Role;

public interface RoleController {
    boolean checkIfGranted(Role role);
}
