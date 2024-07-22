package org.izdevs.acidium.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
//TODO MIGRATE THIS TO SPRING COMPONENT
@Service
public class SessionGenerator {
    static Logger logger = LoggerFactory.getLogger(SessionGenerator.class);
    public volatile Set<UUID> avalibles = new HashSet<>();
    public volatile Set<UUID> valid = new HashSet<>();
    private int rounds = 0;
    @Scheduled(fixedDelay = 100) //10 uuids per second
    protected void session(){
        if(avalibles.size() < 100 && rounds < 20){
            rounds = 0;
            UUID uuid = UUID.randomUUID();
            avalibles.add(uuid);
        }
        else{
            rounds ++;
        }
    }
    public synchronized boolean validate(UUID uuid){
        return valid.contains(uuid) || avalibles.contains(uuid);
    }
    public synchronized UUID use(){
        UUID uuid = avalibles.iterator().next();
        avalibles.remove(uuid);
        valid.add(uuid);
        return uuid;
    }
    public synchronized void destroy(UUID uuid){
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
