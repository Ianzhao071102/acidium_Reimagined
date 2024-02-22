package org.izdevs.acidium.serialization;

import lombok.Getter;
import org.izdevs.acidium.tick.Ticked;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;


public class ResourceFacade implements Ticked {
    static Logger logger = LoggerFactory.getLogger(ResourceFacade.class);
    @Getter
    static ArrayList<Resource> resources = new ArrayList<>();

    @Override
    public void tick() {

    }
    public static void registerAPI(API api){
        api.isApi = true;
        resources.add(api);
    }
    public static void registerResource(Resource resource) {
        resources.add(resource);
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
