package org.izdevs.acidium.networking;

import org.izdevs.acidium.api.v1.Role;
import org.izdevs.acidium.security.RoleController;

public class ServerHandlerRoleController implements RoleController {
    @Override
    public boolean checkIfGranted(Role role) {
        return role.getLevelsGranted().contains(Role.Level.MODERATOR) || role.getLevelsGranted().contains(Role.Level.ADMIN);
    }
}
