package org.izdevs.acidium.serialization.naming;

import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.utils.RandomUtils;
import org.springframework.stereotype.Component;

@Component
public class ResourceNamingService implements NameGenerator{
    @Override
    public String nameObject(Object object) {
        assert object instanceof Resource;

        Resource resource = (Resource) object;
        String simple = resource.getClass().getSimpleName();
        String appendix = RandomUtils.getRandomString(5);

        StringBuilder sb = new StringBuilder();
        sb.append(simple);
        sb.append('-');
        sb.append(appendix);
        return sb.toString();
    }
}
