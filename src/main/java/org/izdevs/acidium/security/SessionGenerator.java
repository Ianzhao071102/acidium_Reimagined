package org.izdevs.acidium.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SessionGenerator {
    static Logger logger = LoggerFactory.getLogger(SessionGenerator.class);
    public static volatile Set<UUID> avalibles = new HashSet<>();
    public static volatile Set<UUID> valid = new HashSet<>();
    @Scheduled(fixedDelay = 100) //10 uuids per second
    public synchronized static void session(){
        UUID uuid = UUID.randomUUID();
        avalibles.add(uuid);
        logger.debug("added session: " + uuid);
    }
    public synchronized static boolean validate(UUID uuid){
        return valid.contains(uuid) || avalibles.contains(uuid);
    }
    public synchronized static UUID use(){
        UUID uuid = avalibles.iterator().next();
        avalibles.remove(uuid);
        valid.add(uuid);
        return uuid;
    }
    public synchronized static void destroy(UUID uuid){
        if(avalibles.contains(uuid) || valid.contains(uuid)){
            //contains
            boolean one = avalibles.remove(uuid);
            boolean sec = valid.remove(uuid);
            if(!one && !sec){
                throw new RuntimeException("the thing does not exist: " + uuid);
            }
        }
    }
}
