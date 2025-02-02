package org.izdevs.acidium;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Getter;
import org.izdevs.acidium.networking.account.UserAccountSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;


@Configuration
@SpringBootApplication
@EnableScheduling
@EntityScan("org.izdevs.acidium")
@EnableAspectJAutoProxy
@EnableJpaRepositories
public class AcidiumApplication extends SpringApplication {
    @Getter
    public static ConfigurableApplicationContext context;

    static Logger logger = LoggerFactory.getLogger(AcidiumApplication.class);
    @Autowired
    @Qualifier("license")
    boolean print_license;

    public static void main(String[] args) {
        context = SpringApplication.run(AcidiumApplication.class, args);
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
