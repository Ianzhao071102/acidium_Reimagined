package org.izdevs.acidium;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Configuration
@EnableScheduling
@Component
public class Config{

    public static final int ticksPerSecond = 20;

}
