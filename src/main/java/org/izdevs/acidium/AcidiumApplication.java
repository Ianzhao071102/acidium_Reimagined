package org.izdevs.acidium;


import org.izdevs.acidium.serialization.ReflectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
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
        for (int i = 0; i <= resources.length - 1; i++) {
            org.springframework.core.io.Resource resource = resources[i];
            List<String> lines = Files.readAllLines(Path.of(resource.getURI()));

            logger.info("---------- BEGIN NOTE ----------");
            for (int j = 0; j <= lines.size() - 1; j++) {
                logger.info(lines.get(j));
            }
            logger.info("---------- END NOTE -----------");
        }
    }
}
