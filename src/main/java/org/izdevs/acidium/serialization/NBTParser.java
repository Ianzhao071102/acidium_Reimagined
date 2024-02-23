package org.izdevs.acidium.serialization;

import com.esotericsoftware.yamlbeans.YamlException;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.api.v1.BlockSpec;
import org.izdevs.acidium.api.v1.CommandDefinition;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.entity.MobHolder;
import org.izdevs.acidium.tick.Ticked;
import org.izdevs.acidium.world.BlockDataHolder;
import org.izdevs.acidium.world.BlockType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.launch.support.RuntimeExceptionTranslator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class NBTParser{
    static Logger logger = LoggerFactory.getLogger(NBTParser.class);
    public static ArrayList<CommandDefinition> definitions = new ArrayList<>();
    public static void registerNBTDef(InputStream raw) throws YamlException {
        try {
            CompoundTag tag = BinaryTags.readCompressed(raw); //top level containing:
            String apiVersion = tag.getString("apiVersion");
            String name = tag.getString("name");
            String type = tag.getString("type");
            switch(type){
                case "mobSpec" -> {
                    CompoundTag spec = tag.getCompound("spec");
                    int bDamage = spec.getInt("bDamage");
                    int health = spec.getInt("health");
                    double speed = spec.getDouble("speed");

                    if(apiVersion.matches("^v+\\d")){
                        //api version legit
                        Mob mob = new Mob(name,speed,health,bDamage);
                        logger.info(mob + " was successfully loaded and is now being registered");
                        MobHolder.registerMob(mob);
                        if(MobHolder.registeredMobs.contains(mob))  logger.info(mob + " was registered");
                        else logger.warn("the mob maybe isn't registered");
                    }
                }
                case "blockSpec" -> {
                    String walkable = tag.getString("walkable");
                    BlockSpec spec = new BlockSpec(tag);
                    spec.setWalkable(walkable.equalsIgnoreCase("true"));

                    try{
                        boolean found = false;
                        for(int i=0;i<=BlockType.values().length-1;i++){
                            if(name.equalsIgnoreCase(BlockType.values()[i].toString())){
                                found = true;
                                break;
                            }
                        }
                        if(!found){
                            throw new IllegalArgumentException();
                        }
                        if(!BlockDataHolder.registered.contains(spec)) {
                            BlockDataHolder.registerSpec(spec);
                        } else{
                            logger.warn("spec is already registered...");
                        }
                    }catch(IllegalArgumentException e){
                        throw new RuntimeException("please render a valid name for the blockSpec, it must match a existing name in:" + Arrays.toString(BlockType.values()));
                    }
                }

            }

            //```
            //apiVersion: v1
            //kind: MobSpec
            //metadata:
            //  name: "Hydrogen"
            //spec:
            //  health: 5
            //  speed: 1
            //  bDamage: 50
            //  Additional: None
            //```
        }catch(Throwable throwable){
            logger.error(String.valueOf(throwable));
            throw new RuntimeException(throwable);
        }
    }

}
