package org.izdevs.acidium.basic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ManifestHolder {
    @Getter
    @Setter
    static Set<URI> manifests = new HashSet<>();

    public static void init() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/*.yaml");
        Logger logger = LoggerFactory.getLogger(ManifestHolder.class);

        logger.debug("found " + resources.length + " manifests on classpath resources folder..");
        for(int i=0;i<=resources.length-1;i++){
            Resource resource = resources[i];
            String line = Files.readString(Path.of(resource.getURI()));
            Gson gson = new Gson();
            Type mapType = new TypeToken<Map<String, URI>>() {}.getType();
            Map<String,URI> map = gson.fromJson(line,mapType);
            if(map == null){
                logger.debug("map failed to parse because it is empty...");
                continue;
            }
            logger.debug("parsing: " + line);
            for(int j=0;j<=map.size()-1;j++){
                URI current = map.get(map.keySet().iterator().next());
                manifests.add(current);
            }
        }
    }
}
