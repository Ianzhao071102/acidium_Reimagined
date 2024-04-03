package org.izdevs.acidium.entity;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.izdevs.acidium.basic.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.izdevs.acidium.utils.LoggingUtils.panic;

@NoArgsConstructor
@Getter
public class EntityHolder {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @PostConstruct
    public void initialize(UserRepository repository) {
        logger.info("initializing entity holder...");
        if (repository == null) panic("could not find entity repository", this.getClass());
        else {


            logger.info("initialized entity holder...");
        }
    }
}
