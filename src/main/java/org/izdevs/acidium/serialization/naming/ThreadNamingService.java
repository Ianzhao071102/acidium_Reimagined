package org.izdevs.acidium.serialization.naming;

import org.izdevs.acidium.utils.RandomUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class ThreadNamingService implements NameGenerator{
    @Override
    public String nameObject(Object object) {
        byte[] seed = object.toString().getBytes(StandardCharsets.UTF_8);
        return "Thread" +
                '-' +
                RandomUtils.getRandomString(seed,4);
    }
}
