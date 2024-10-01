package org.izdevs.acidium;

public interface CentralUtil {
    void crash(String message,Exception reason,boolean force);
    void crash(Exception reason,boolean force);
    void crash(boolean force);
}
