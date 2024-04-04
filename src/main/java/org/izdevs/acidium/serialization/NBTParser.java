package org.izdevs.acidium.serialization;

import com.esri.core.geometry.Point;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.re2j.Matcher;
import com.google.re2j.Pattern;
import net.forthecrown.nbt.BinaryTag;
import net.forthecrown.nbt.BinaryTags;
import net.forthecrown.nbt.CompoundTag;
import net.forthecrown.nbt.ListTag;
import org.izdevs.acidium.api.v1.BlockSpec;
import org.izdevs.acidium.api.v1.CommandDefinition;
import org.izdevs.acidium.api.v1.Mob;
import org.izdevs.acidium.api.v1.Structure;
import org.izdevs.acidium.entity.MobHolder;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingRecipeHolder;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.equipment.EquipmentHolder;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.world.Block;
import org.izdevs.acidium.world.BlockDataHolder;
import org.izdevs.acidium.world.BlockType;
import org.izdevs.acidium.world.StructureHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class NBTParser {
    static Logger logger = LoggerFactory.getLogger(NBTParser.class);
    public static ArrayList<CommandDefinition> definitions = new ArrayList<>();

    public static void registerNBTDef(InputStream raw) {
        try {
            CompoundTag tag = BinaryTags.readCompressed(raw); //top level
            String apiVersion = tag.getString("apiVersion");
            String name = tag.getString("name");
            String type = tag.getString("type");
            switch (type) {
                case "mobSpec" -> {
                    CompoundTag spec = tag.getCompound("spec");
                    int bDamage = spec.getInt("bDamage");
                    int health = spec.getInt("health");
                    double speed = spec.getDouble("speed");
                    int hitboxRadius = spec.getInt("hitboxRadius");
                    if (apiVersion.matches("^v+\\d")) {
                        //api version legit
                        Mob mob = new Mob(name, speed, health, bDamage, hitboxRadius);
                        logger.info(mob + " was successfully loaded and is now being registered");
                        MobHolder.registerMob(mob);
                        if (MobHolder.registeredMobs.contains(mob)) logger.info(mob + " was registered");
                        else logger.warn("the mob maybe isn't registered");
                    }
                }
                case "blockSpec" -> {
                    String walkable = tag.getString("walkable");
                    BlockSpec spec = new BlockSpec(tag);
                    spec.setWalkable(walkable.equalsIgnoreCase("true"));

                    try {
                        boolean found = false;
                        for (int i = 0; i <= BlockType.values().length - 1; i++) {
                            if (name.equalsIgnoreCase(BlockType.values()[i].toString())) {
                                found = true;
                                break;
                            }
                        }
                        if (!found) {
                            throw new IllegalArgumentException();
                        }
                        if (!BlockDataHolder.registered.contains(spec)) {
                            BlockDataHolder.registerSpec(spec);
                        } else {
                            logger.warn("spec is already registered...");
                        }
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("please render a valid name for the blockSpec, it must match a existing name in:" + Arrays.toString(BlockType.values()));
                    }
                }

                case "structure" -> {
                    ListTag blocks = tag.getList("blocks");
                    Map<Point, Block> current = new HashMap<>();
                    for (int i = 0; i <= blocks.size() - 1; i++) {
                        String plr = blocks.getString(i);

                        logger.debug("received dara: " + plr);
                        Gson gson = new GsonBuilder().create();

                        Type mapType = new TypeToken<Block>() {
                        }.getType();

                        int x = 0, y = 0;
                        Block block = gson.fromJson(plr, mapType);
                        x = block.getX();
                        y = block.getY();

                        Point point = new Point(x, y);

                        current.put(point, block);
                    }
                    //create with specified name...
                    Structure structure = new Structure(name);

                    //register to holder...
                    structure.setDescription(current);
                    StructureHolder.register(structure);
                }

                case "user" -> {
                    //anti injection is included...
                    String username = tag.getString("username");
                    String uuid = tag.getString("uuid");
                    String passwordHash = tag.getString("password_hash");
                    if (username == null || uuid == null || passwordHash == null) {
                        throw new IllegalArgumentException("username, uuid and password hash could NOT be null");
                    } else {
                        //no fucked data found

                        //regex pattern for username
                        Pattern pattern = Pattern.compile("^[a-z]|[A-Z]|[0-9]{5,20}$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
                        Matcher username_match = pattern.matcher(username);

                        //regex pattern for uuid
                        Matcher uuid_match = Pattern.compile("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$").matcher(uuid);
                        if (username_match.matches() && uuid_match.matches()) {
                            //matches till here, good to go

                        } else {
                            throw new IllegalArgumentException("username or password hash is illegal...");
                        }
                    }
                }

                case "crafting_recipe" -> {
                    String recipe_name = tag.getString("crafting_recipe_name");
                    ListTag ingredients = tag.getList("ingredients");
                    boolean ordered = tag.getInt("ordered") >= 1;
                    boolean creatable = tag.getInt("craftable") >= 1;
                    //todo finish recipe construction method impl

                    if (ingredients.isEmpty()) {
                        throw new IllegalArgumentException("invalid compound ingredients: list 'ingredients' is EMPTY");
                    }

                    Set<CraftingSlot> slots = new HashSet<>();

                    //item initialized should be -1 based on testings
                    int x = -1, y = -1;
                    for (int i = 0; i <= ingredients.size() - 1; i++) {
                        BinaryTag _this = ingredients.get(i);
                        if (_this.isString()) {
                            String ingredient_name = _this.asString().toString();
                            int j = 0;
                            for (Equipment _this_ : EquipmentHolder.getEquipments()) {
                                if (_this_.getName().equals(ingredient_name)) {
                                    if (ordered) {
                                        x++;
                                        y++;
                                        j++;
                                        slots.add(new CraftingSlot(x, y, _this_));
                                        logger.debug("added crafting slot at:" + j);

                                    } else {
                                        slots.add(new CraftingSlot(_this_));
                                    }
                                }
                                throw new IllegalArgumentException("invalid compound ingredients: list 'ingredients' is INVALID");
                            }
                        } else {
                            throw new IllegalArgumentException("invalid compound ingredients: list 'ingredients' is INVALID");

                        }
                    }
                    CraftingRecipe recipe = new CraftingRecipe(recipe_name,ordered, creatable, slots);
                    CraftingRecipeHolder.registerRecipe(recipe);
                }

                case "equipment" -> {
                    String crafting_recipe_name = tag.getString("recipe_name");
                    String equipment_name = tag.getString("equipment_name");
                    ListTag allowedSlots = tag.getList("slots");
                    for(int i=0;i<=allowedSlots.size()-1;i++){
                        String inventory_slot_name = allowedSlots.getString(i);
                        boolean found = false;
                        for(InventoryType a: InventoryType.values()){
                            if(inventory_slot_name.equals(a.name())){
                                found = true;
                                Equipment equipment = new Equipment(equipment_name);
                                CraftingRecipe recipe = null;
                                for(CraftingRecipe _recipe:CraftingRecipeHolder.getRecipes()){
                                    if(_recipe.getName().equalsIgnoreCase(crafting_recipe_name)){
                                        recipe = _recipe;
                                    }
                                }
                                if(recipe == null){
                                    throw new IllegalArgumentException("fucked...");
                                }

                                EquipmentHolder.register(equipment);
                            }
                        }
                        if(!found){
                            throw new IllegalArgumentException("fucked...");
                        }
                    }
                }
            }
        } catch (Throwable throwable) {
            logger.error(String.valueOf(throwable));
            throw new RuntimeException(throwable);
        }
    }

}
