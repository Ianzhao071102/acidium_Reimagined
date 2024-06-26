package org.izdevs.acidium.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtils {
    public static void panic(String data,Class<?> caller){
        Logger logger = LoggerFactory.getLogger(caller);
        logger.error(data);
    }

    public static void die(String message,Class<?> caller){
        Logger logger = LoggerFactory.getLogger(caller);

        logger.error(message);
        throw new UnsupportedOperationException();
    }
  
}
