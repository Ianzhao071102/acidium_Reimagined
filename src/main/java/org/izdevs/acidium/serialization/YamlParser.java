package org.izdevs.acidium.serialization;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import org.izdevs.acidium.api.v1.CommandDefinition;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.tick.Ticked;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static org.izdevs.acidium.entity.MobHolder.registerMob;

public class YamlParser implements Ticked {
    public static ArrayList<CommandDefinition> definitions = new ArrayList<>();
    public static void registerYamlDef(InputStream raw) throws YamlException {
        YamlReader reader = new YamlReader(new InputStreamReader(raw));
        Object object = reader.read();
        System.out.println(object);
        Map map = (Map)object;

        String api = (String) map.get("apiVersion");
        String type = (String) map.get("type");
        Map metadata = (Map) map.get("metadata");
        Map spec = (Map) map.get("spec");

        if(!api.matches("^v+\\d")){
            throw new IllegalArgumentException("apiVersion field is invalid");
        }
        String name = (String) metadata.get("name");

        switch(type){
            case "mobSpec"-> {
                //TODO FINISH MOB PARSING
                int health = Integer.getInteger((String) spec.get("health"));
                double speed = Double.parseDouble((String) spec.get("speed"));
                int bDamage = Integer.getInteger((String) spec.get("bDamage"));
                Mob mob = new Mob(name,speed,health,bDamage);
                registerMob(mob);
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
    }
    @Override
    public void tick() {
        //TODO REGISTER DEFINITIONS EVERY TICK
    }
}
