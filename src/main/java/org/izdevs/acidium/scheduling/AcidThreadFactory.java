package org.izdevs.acidium.scheduling;

import lombok.NonNull;
import org.izdevs.acidium.serialization.naming.ThreadNamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

@Service
public class AcidThreadFactory extends CustomizableThreadFactory {
    @Autowired
    ThreadNamingService naming;
    @Override
    public @NonNull Thread newThread(@NonNull Runnable runnable) {
        return new Thread(runnable,naming.nameObject(runnable));
    }
}
