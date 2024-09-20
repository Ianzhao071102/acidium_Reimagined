package org.izdevs.acidium.serialization.asset;

import lombok.extern.slf4j.Slf4j;
import org.izdevs.acidium.serialization.ResourceLocator;
import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class DefaultAssetLocator implements ResourceLocator {
    //this thing might have some performance issues
    @Override
    public URI getURI(String namespace, String typeName, String name) {
        if (!namespace.equalsIgnoreCase("internal.builtin")) {
            throw new UnsupportedOperationException("for assets, namespace should be internal.builtin.asset");
        }
        if (!typeName.equalsIgnoreCase("asset")) {
            throw new UnsupportedOperationException("typeName should be: asset");
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        AtomicInteger rev = new AtomicInteger();
        AtomicReference<URI> uri = new AtomicReference<>();
        try {
            Arrays.stream(resolver.getResources("classpath:/game/**.**")).forEach((a) -> {
                try {
                    if(rev.get() >= 1){
                        return;
                    }
                    if(a.getFile().exists()){
                        if(Objects.requireNonNull(a.getFilename()).equalsIgnoreCase("__METADATA__.json")){
                            //matched metadata.json
                            log.debug(a.getContentAsString(Charset.defaultCharset()));
                        }

                        if(a.getFile().getName().equalsIgnoreCase(name)){
                            uri.set(a.getURI());
                            rev.getAndIncrement();
                        }
                    }
                    else throw new RuntimeException("failed to decode json: file does not exist");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (IOException e) {
            log.error("failed to locate built-in asset:" + e);
        }
        if(uri.get() == null){
            log.error("failed to find uri");
            throw new IllegalArgumentException("uri of asset is not found for: " + name);
        }
        return uri.get();
    }

    @Override
    public ResourceSchema[] locate() {
        return new ResourceSchema[0];
    }
}
