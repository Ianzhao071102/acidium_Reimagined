package org.izdevs.acidium.serialization.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.serialization.API;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class ResourceSchema {
    @GeneratedValue
    @Id
    private Long id;

    String name;

    @Lob
    CompoundTag tag;
    boolean isApi;

    String apiName;
}
