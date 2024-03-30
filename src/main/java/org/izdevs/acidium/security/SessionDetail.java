package org.izdevs.acidium.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.api.v1.Role;

@Getter
@Setter
@AllArgsConstructor
public class SessionDetail {
    String location;
    Role role;
}
