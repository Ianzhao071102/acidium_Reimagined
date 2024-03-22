package org.izdevs.acidium;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
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
    public DataSource getDataSource() {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String pwd = env.getProperty("spring.datasource.password");
        DataSource _this = DataSourceBuilder.create().username(username).url(url).password(pwd).build();

        try {
            _this.getConnection();

        } catch (SQLException e) {
            logger.warn("failed to connect to sql...");
            logger.warn("using embedded psql...");

            _this = DataSourceBuilder.create().username("sa").password("password").driverClassName("org.h2.Driver").url("jdbc:h2:file:/data/acidium").build();
        }

        logger.info("connection to sql has been verified to be legitimate...");
        logger.debug(_this.toString());
        return _this;
    }


    @Bean(name = "port")
    @Lazy(false)

    public int port(){
        return Integer.parseInt(Objects.requireNonNull(env.getProperty("port")));
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //this is a filter chain...
        http.cors(Customizer.withDefaults()); // disable this line to reproduce the CORS 401
        return http.build();
    }
}
