package org.izdevs.acidium.security;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.izdevs.acidium.api.v1.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service("roleManager")
public class RoleManager {
    @Autowired
    RoleRepository repository;

    @Getter
    @Autowired
    public volatile Set<Role> roles = new HashSet<>();

    @PostConstruct
    public void readRepo(){
        repository.findAll().forEach(role -> {
            roles.add(role);
        });
    }
}
