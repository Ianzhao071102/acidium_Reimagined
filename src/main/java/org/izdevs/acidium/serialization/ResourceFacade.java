package org.izdevs.acidium.serialization;

import lombok.Getter;
import org.izdevs.acidium.serialization.exceptions.ResourceNotFoundException;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@Service
public abstract class ResourceFacade{
    @Autowired
    ResourceSchemaRepository repository;

    static Logger logger = LoggerFactory.getLogger(ResourceFacade.class);
    @Getter
    static ArrayList<Resource> resources = new ArrayList<>();


    public void registerAPI(API api){
        api.isApi = true;
        repository.save(api.schema);
    }
    public void registerResource(Resource resource) {
        resources.add(resource);
        repository.save(resource.schema);
    }

    public static void start() throws InstantiationException, IllegalAccessException {

        Reflections reflections = new Reflections("org.izdevs.acidium");

        Set<Class<?>> subTypes =
                reflections.get(SubTypes.of(API.class).asClass());
        for (Class<?> subType : subTypes) {
            logger.debug(subType.newInstance().toString());
        }
    }
}
