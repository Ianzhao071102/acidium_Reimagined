package org.izdevs.acidium.serialization;

import org.izdevs.acidium.serialization.models.ResourceSchema;

import java.net.URI;

public interface ResourceLocator {
    URI getURI(String namespace,String typeName,String name);
    ResourceSchema[] locate();
}
