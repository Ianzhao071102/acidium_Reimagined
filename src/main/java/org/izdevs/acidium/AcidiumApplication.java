package org.izdevs.acidium;


import org.izdevs.acidium.scheduling.LoopManager;
import org.izdevs.acidium.scheduling.ScheduledTask;
import org.izdevs.acidium.serialization.SerializerFactory;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;



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

        LoopManager.registerRepeatingTask(new ScheduledTask(() -> {
            logger.info("test from task");
        }));

        logger.error(bcrypt("Yqsz071102"));
    }


    public static String bcrypt(String string) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
        return encoder.encode(string);
    }

    public static void readAndPrintNote() throws IOException {
        org.springframework.core.io.Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:/*.note");
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
