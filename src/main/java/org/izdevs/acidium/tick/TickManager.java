package org.izdevs.acidium.tick;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import static org.reflections.scanners.Scanners.SubTypes;

public class TickManager {
    public static void init() throws NoSuchMethodException {
        Logger logger = LoggerFactory.getLogger(TickManager.class);
        logger.info("Tick Manager has been deprecated");
    }

}
