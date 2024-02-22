package org.izdevs.acidium.serialization;

import com.esotericsoftware.yamlbeans.YamlException;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import org.izdevs.acidium.api.v1.CommandDefinition;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.entity.MobHolder;
import org.izdevs.acidium.tick.Ticked;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;

public class NBTParser implements Ticked {
    static Logger logger = LoggerFactory.getLogger(NBTParser.class);
    public static ArrayList<CommandDefinition> definitions = new ArrayList<>();
    public static void registerNBTDef(InputStream raw) throws YamlException {
        try {
            CompoundTag tag = BinaryTags.readCompressed(raw); //top level containing:
            String apiVersion = tag.getString("apiVersion");
            String name = tag.getString("name");

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
    @Override
    public void tick() {
        //TODO REGISTER DEFINITIONS EVERY TICK
    }

}
