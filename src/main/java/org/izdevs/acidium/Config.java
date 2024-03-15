package org.izdevs.acidium;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Objects;

@PropertySources({
       @PropertySource("classpath:application.properties"),
       @PropertySource("classpath:server.properties")
})
@Configuration
@EnableScheduling
@Component
public class Config{

    @Autowired
    Environment env;

    public static final int ticksPerSecond = 20;

    @Bean(name = "maxPlayers")
    @Lazy(false)
    public static int getMaxPlayers(){
        return 20;
    }

    @Bean(name = "psql")
    @Lazy(false)

    public DataSource getDataSource(){
        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String pwd = env.getProperty("spring.datasource.password");
        return DataSourceBuilder.create().username(username).url(url).password(pwd).build();
    }

    @Bean(name = "port")
    @Lazy(false)

    public int port(){
        return Integer.parseInt(Objects.requireNonNull(env.getProperty("port")));
    }
}
