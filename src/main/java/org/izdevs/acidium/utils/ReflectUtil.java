package org.izdevs.acidium.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class ReflectUtil {
    public static Resource[] getNBTResources() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources("classpath*:**/*.nbt");
    }

    public static Resource[] getJSONResources() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        return resolver.getResources("classpath*:**/*.json");
    }
    public static List<String> getLicense() throws IOException {
        return Files.readAllLines(Path.of(new PathMatchingResourcePatternResolver().getResource("LICENSE.txt").getURI()));
    }
}
