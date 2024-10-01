package org.izdevs.acidium;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CentralUtilImpl implements CentralUtil {
    @Override
    public void crash(String message, Exception reason, boolean force) {
        log.error("crashing with message: " + message, reason);
        if (force) {
            log.error("force crashing");
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void crash(Exception reason, boolean force) {
        log.error("crashing with exception: " + reason);
        if (force) {
            log.error("force crashing");
            throw new UnsupportedOperationException();
        }


    }
    @Override
    public void crash(boolean force) {
        log.error("force crashing server");
        if (force) throw new UnsupportedOperationException();
    }
}