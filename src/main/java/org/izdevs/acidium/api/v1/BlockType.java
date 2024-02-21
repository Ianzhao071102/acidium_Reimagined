package org.izdevs.acidium.api.v1;

import lombok.Getter;
import lombok.Setter;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.SpecObject;

import java.util.ArrayList;
import java.util.Arrays;

@Getter
public class BlockType extends Resource {
    @Setter
    ArrayList<String> bTypes = new ArrayList<>();
    public BlockType(String name) {
        super(name, false);
    }
    public BlockType(String... whatType){
        super("BlockType",false);
        ArrayList<String> types = new ArrayList<>(Arrays.asList(whatType).subList(0, whatType.length + 1));
        bTypes.addAll(Arrays.asList(whatType).subList(0, whatType.length + 1));
        ArrayList<SpecObject> objects = new ArrayList<>();
        objects.add(new SpecObject("types", Arrays.toString(types.toArray())));
        this.spec = objects;
    }
}
