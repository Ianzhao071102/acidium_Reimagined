package org.izdevs.acidium.command;

import org.izdevs.acidium.api.v1.CommandDefinition;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.api.v1.DefaultSpawner;
import org.izdevs.acidium.entity.AbstractMobSpawner;
import org.izdevs.acidium.serialization.API;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceFacade;
import org.izdevs.acidium.tick.Ticked;
import org.yaml.snakeyaml.Yaml;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YamlParser implements Ticked {
    public static ArrayList<CommandDefinition> definitions = new ArrayList<>();
    public static void registerYamlCommandDef(String raw){
        final Yaml yaml = new Yaml();


        //vulnerable code
        Map<String, Object> obj = yaml.load(raw);
        //end vulnerable

        if(obj.containsKey("apiVersion") && obj.containsKey("kind") && obj.containsKey("spec") && obj.containsKey("metadata")){
            //basic check last resort
            String api = (String) obj.get("apiVersion");
            Pattern pattern = Pattern.compile("^v+\\d");
            Matcher matcher = pattern.matcher(api);
            boolean matchFound = matcher.find();
            if(matchFound){
                API found = null;
                for(int i=0;i<=ResourceFacade.getResources().size();i++){
                    Resource thisThing = ResourceFacade.getResources().get(i);
                    if(thisThing.isApi()){
                        if(thisThing.getTypeName().equals(api)){
                            found = (API) thisThing;
                            break;
                        }
                    }
                }
                if(found == null){
                    throw new IllegalArgumentException("please review malformed yaml, the api requested: "+ api + "is not found in :" + raw);
                }
                String type = (String) obj.get("type");
                switch(type){
                    case "Mob" -> {
                        String name = null;
                        String health = null;
                        String speed = null;
                        String bDamage = null;
                        //api associated is found
                        try {
                            health = (String) obj.get("health");
                            speed = (String) obj.get("speed");
                            bDamage = (String) obj.get("bDamage");
                            name = (String) obj.get("name");
                        } catch (Throwable e) {
                            throw new IllegalArgumentException("please review malformed yaml, must follow specs to continue: " + raw);
                        }

                        if (health == null || speed == null || bDamage == null) {
                            throw new IllegalArgumentException("please review malformed yaml, must follow specs to continue: " + raw);
                        }

                        Mob mob = null;
                        try {
                            //now mob is ready to be initialized
                            mob = new Mob(name, Double.parseDouble(speed), Integer.parseInt(health), Integer.parseInt(bDamage));
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("please review malformed yaml, must follow specs to continue: " + raw);
                        }

                        String spawner = (String) obj.get("spawner");
                        if (spawner == null) {
                            //assign it to default spawner
                            DefaultSpawner.jobQueue.add(mob);
                        } else {
                            final Resource spawner1 = getResource();
                            if(spawner1 == null){
                                throw new IllegalArgumentException("spawner specified not found : " + raw);
                            }
                            if(spawner1 instanceof AbstractMobSpawner spawner2){
                                //now let the spawner spawn
                                spawner2.addJob(mob);
                            }else{
                                throw new IllegalArgumentException("spawner specified not found : " + raw);
                            }

                        }
                        //anti un-nested gross helper
                        //in switch definition for case: "mob"
                    }

                    //TODO ADD MORE CASES TO PARSE MORE YAML FROM SHIT
                }






            }
        }else{
            throw new IllegalArgumentException("please review malformed yaml, must follow specs to continue: " + raw);
        }
    }

    private static Resource getResource() {
        Resource spawner1 = null;
        ArrayList<Resource> resources = ResourceFacade.getResources();
        for (int i = 0; i <= resources.size(); i++) {
            for (int j = 0; j <= resources.get(i).getFlags().size(); j++) {
                String flag = resources.get(i).getFlags().get(i);
                if (flag.equals("spawner")) {
                    spawner1 = resources.get(i);
                    break;
                }
            }
        }
        return spawner1;
    }

    @Override
    public void tick() {
        //TODO REGISTER DEFINITIONS EVERY TICK
    }
}
