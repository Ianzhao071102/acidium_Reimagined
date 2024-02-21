package org.izdevs.acidium.serialization;


import org.springframework.stereotype.Component;

@Component
public class Serializer {
    public static String serializeResource(Resource resource){
        return resource.toString();
    }
}
