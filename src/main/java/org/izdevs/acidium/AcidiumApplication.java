package org.izdevs.acidium;


import org.apache.naming.factory.ResourceFactory;
import org.izdevs.acidium.utils.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.izdevs.acidium.serialization.NBTParser.registerNBTDef;

@Configuration
@SpringBootApplication
@EnableScheduling
@EntityScan("org.izdevs.acidium")
public class AcidiumApplication extends SpringApplication {
    static Logger logger = LoggerFactory.getLogger(AcidiumApplication.class);
    @Autowired
    @Qualifier("license")
    boolean print_license;

    public static void main(String[] args) {
        SpringApplication.run(AcidiumApplication.class, args);
    }


    public static void loadNBT() throws IOException {
        org.springframework.core.io.Resource[] resource = ReflectUtil.getResources();
        if (resource.length == 0) {
            logger.info(resource.length + " resources was/were found");
            logger.debug("no nbt file found on classpath");
            return;
        }
        for (int i = 0; i <= resource.length - 1; i++) {
            org.springframework.core.io.Resource resource1 = resource[i];
            InputStream stream = resource1.getInputStream();
            registerNBTDef(stream);
            logger.info("registered nbt def: " + resource1.getFilename());
        }
    }


    public static String bcrypt(String string) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode(string);
    }

    public static void readAndPrintNote() throws IOException {
        org.springframework.core.io.Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/*.note");
        printResource(resources);

        resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/*.txt");
        printResource(resources);
    }


    private static void printResource(Resource[] resources) throws IOException {
        for (int i = 0; i <= resources.length - 1; i++) {
            Resource resource = resources[i];
            URI uri = resource.getURI();
            Path path = Path.of(uri);

            List<String> lines = Files.readAllLines(path);

            logger.info("---------- BEGIN NOTE ----------");
            for (int j = 0; j <= lines.size() - 1; j++) {
                logger.info(lines.get(j));
            }
            logger.info("---------- END NOTE -----------");
        }
    }
}
