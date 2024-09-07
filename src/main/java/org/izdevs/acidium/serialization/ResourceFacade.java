package org.izdevs.acidium.serialization;

import com.google.gson.Gson;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

@Service
public class ResourceFacade{
    Gson gson = new Gson();
    static boolean init = false;
    @Autowired
    ResourceNamingService __naming;

    static ResourceNamingService naming;
    private static final StaticListableBeanFactory factory = new StaticListableBeanFactory();
    @Autowired
    Set<Resource> resources;

    private static final Set<ResourceSchema> write_later = new HashSet<>();
    static Logger logger = LoggerFactory.getLogger(ResourceFacade.class);

    @EventListener(ApplicationReadyEvent.class)
    public void write(){
        if(!write_later.isEmpty()){
            for(int i=0;i<= write_later.size()-1;i++){
                ResourceSchema resource = write_later.iterator().next();
                factory.addBean(naming.nameObject(resource),resource);
            }
        }
    }
    public void registerResource(ResourceSchema resource){
        logger.debug("registered resource"); 
        logger.debug(gson.toJson(resource));
        if(!init){
            write_later.add(resource);
        }else {
            factory.addBean(naming.nameObject(resource), resource);
        }
    }
    public static void registerResource_static(ResourceSchema resource){
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
    public <T> Map<String, T> getResourceBeans(Class<T> type){
        return factory.getBeansOfType(type);
    }
}
