package org.izdevs.acidium;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
@EnableScheduling
@Component
public class Config{

    public static final int ticksPerSecond = 20;

    @Bean
    public static int getMaxPlayers(){
        return 20;
    }

    @ConfigurationProperties(prefix="spring.datasource")
    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder
                .create()
                .build();
    }
}
