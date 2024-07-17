package org.izdevs.acidium.serialization;

import lombok.Getter;
import org.izdevs.acidium.serialization.exceptions.ResourceNotFoundException;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@Service
public class ResourceFacade{
    @Autowired
    Set<ResourceSchema> resources;

    @Autowired
    ResourceSchemaRepository repository;

    static Logger logger = LoggerFactory.getLogger(ResourceFacade.class);

    @EventListener(ApplicationReadyEvent.class)
    public void write(){
        for(int i=0;i<=resources.size()-1;i++){
            repository.save(resources.iterator().next());
        }
    }

}
