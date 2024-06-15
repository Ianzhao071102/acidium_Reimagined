package org.izdevs.acidium.serialization;

import org.izdevs.acidium.serialization.models.ResourceSchema;
import org.springframework.data.repository.CrudRepository;

public interface ResourceSchemaRepository extends CrudRepository<ResourceSchema,Long> {
    ResourceSchema findByName(String name);
    ResourceSchema findById(long id);
}
