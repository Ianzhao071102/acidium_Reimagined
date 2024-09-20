package org.izdevs.acidium.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ReflectUtil {
    public static Resource[] getNBTResources() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources("classpath*:**/*.nbt");
    }

    public static Resource[] getJSONResources() throws IOException {
        List<Resource> result = new ArrayList<>();

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] core = resolver.getResources("classpath*:/game/**.json");
        int size = core.length;
        for(Resource r: core){
            if(!Objects.requireNonNull(r.getFilename()).equalsIgnoreCase("data.json")){
                result.add(r);
            }
        }
        //fuck overflow
        Resource[] returner = new Resource[size];
        returner = result.toArray(returner);
        return returner;
    }
    public static List<String> getLicense() throws IOException {
        return Files.readAllLines(Path.of(new PathMatchingResourcePatternResolver().getResource("LICENSE.txt").getURI()));
    }
}
