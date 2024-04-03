package org.izdevs.acidium.entity;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.izdevs.acidium.basic.EntityRepository;

@NoArgsConstructor
@Getter
public class EntityHolder {
    @PostConstruct
    public void initialize(EntityRepository repository){

    }
}
