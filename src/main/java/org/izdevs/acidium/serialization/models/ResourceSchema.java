package org.izdevs.acidium.serialization.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.izdevs.acidium.serialization.SerializableCompoundTag;
import org.springframework.stereotype.Component;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "additional")
public class ResourceSchema {
    @GeneratedValue
    @Id
    private Long id;

    String name;

    @Column(columnDefinition = "json")
    @Lob
    SerializableCompoundTag tag;
    boolean isApi;

    String apiName;

}
