package org.izdevs.acidium.security;

import lombok.Getter;
import org.izdevs.acidium.api.v1.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RoleManager {
    @Autowired
    RoleRepository repository;

    @Getter
    public volatile Set<Role> roles = new HashSet<>();

    public void registerRole(Role role){
        if(roles.contains(role)){
            Logger logger = LoggerFactory.getLogger(RoleManager.class);
            logger.warn("role is already registered... this action will not be performed...");
            logger.warn("stack trace is below:");
            logger.info(new Exception().getLocalizedMessage());
            logger.warn("end stack trace...");
        }else{
            roles.add(role);
            repository.save(role);
        }
    }
}
