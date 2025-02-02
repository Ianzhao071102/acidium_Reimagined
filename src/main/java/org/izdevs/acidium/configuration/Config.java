package org.izdevs.acidium.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.session.web.http.HttpSessionIdResolver;
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
@EnableRedisHttpSession
public class Config {
    @Value(value = "${generateUponStart}")
    boolean generateWorld;
  
    @Bean(name = "generateWorld")
    public boolean generateWorldUponStart() {
        return true;
    }


    @Autowired
    Environment env;

    public static final int ticksPerSecond = 20;

    @Bean(name = "maxPlayers")
    @Lazy(false)
    public static int getMaxPlayers() {
        return 20;
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //this is a filter chain...
        http.cors(Customizer.withDefaults()); // disable this line to reproduce the CORS 401
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public HttpSessionIdResolver httpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }

    @Bean(name = "ticksPerSecond")
    public int getTicksPerSecond() {
        return ticksPerSecond;
    }

    @Value( "${version}" )
    static String version;
    @Bean(name = "acidium_version")
    public static String version(){
        return version;
    }

    @Bean(name = "credits")
    public static String getCredits(){
        return "izdevs,jerry(dphater) and the rest of the team";
    }

    @Value("${misc.print_license}")
    static boolean license;
    @Bean(name = "license")
    public boolean generateLicense(){
        return license;
    }
}
