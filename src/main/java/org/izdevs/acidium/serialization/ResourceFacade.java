package org.izdevs.acidium.serialization;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.izdevs.acidium.serialization.exceptions.ResourceNotFoundException;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.izdevs.acidium.serialization.naming.ResourceNamingService;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.StaticListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@Service
public class ResourceFacade{
    static boolean init = false;
    @Autowired
    ResourceNamingService __naming;

    static ResourceNamingService naming;
    private static StaticListableBeanFactory factory = new StaticListableBeanFactory();
    @Autowired
    Set<ResourceSchema> resources;

    @Autowired
    ResourceSchemaRepository repository;
    private static final Set<Resource> write_later = new HashSet<>();
    static Logger logger = LoggerFactory.getLogger(ResourceFacade.class);

    @EventListener(ApplicationReadyEvent.class)
    public void write(){
        init = true;
        for(int i=0;i<=resources.size()-1;i++){
            repository.save(resources.iterator().next());
        }

        if(!write_later.isEmpty()){
            for(int i=0;i<= write_later.size()-1;i++){
                Resource resource = write_later.iterator().next();
                factory.addBean(naming.nameObject(resource),resource);
            }
        }
    }
    public void registerResource(Resource resource){
        if(!init){
            write_later.add(resource);
        }else {
            factory.addBean(naming.nameObject(resource), resource);
        }
    }
    public void registerResource_static(Resource resource){
        if(!init){
            write_later.add(resource);
        }else {
            factory.addBean(naming.nameObject(resource), resource);
        }
    }
    @PostConstruct
    public void init(){
        naming = __naming;
    }
}
