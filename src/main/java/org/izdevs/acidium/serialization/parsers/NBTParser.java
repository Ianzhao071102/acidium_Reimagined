package org.izdevs.acidium.serialization.parsers;

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
import org.izdevs.acidium.api.v1.*;
import org.izdevs.acidium.game.crafting.CraftingRecipe;
import org.izdevs.acidium.game.crafting.CraftingRecipeHolder;
import org.izdevs.acidium.game.crafting.CraftingSlot;
import org.izdevs.acidium.game.equipment.DropTable;
import org.izdevs.acidium.game.equipment.Equipment;
import org.izdevs.acidium.game.equipment.EquipmentHolder;
import org.izdevs.acidium.game.inventory.InventoryType;
import org.izdevs.acidium.serialization.Resource;
import org.izdevs.acidium.serialization.ResourceDeserializer;
import org.izdevs.acidium.world.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.*;

@Service
public class NBTParser implements ResourceDeserializer {
    public static ArrayList<CommandDefinition> definitions = new ArrayList<>();
    static Logger logger = LoggerFactory.getLogger(NBTParser.class);

    public static void registerNBTDef(InputStream raw) {
        logger.debug("called with:" + raw);
        throw new UnsupportedOperationException("NBT Parser's deprecated API is called...");
    }

    @Deprecated
    @Override
    public Resource deserialize(String data) {
        throw new UnsupportedOperationException("NBTParser Cannot read data from a string");
    }

    @Override
    public Resource deserialize(InputStream input) {
        Resource result = null;
        try {
            CompoundTag tag = BinaryTags.readCompressed(input);
            String apiVersion = tag.getString("apiVersion");
            String name = tag.getString("name");
            String type = tag.getString("type");

            //mob spec
            //block spec
            //structure
            //user
            //crafting_recipe
            //equipment

            switch (type) {
                case "mobSpec" -> {
                    CompoundTag spec = tag.getCompound("spec");
                    int bDamage = spec.getInt("bDamage");
                    int health = spec.getInt("health");
                    double speed = spec.getDouble("speed");
                    int hitboxRadius = spec.getInt("hitboxRadius");
                    String dropTable = spec.getString("dropTable");
                    if (apiVersion.matches("^v+\\d")) {
                        //api version legit
                        Mob mob = new Mob(name, speed, health, bDamage, hitboxRadius);


                        //enables drop table
                        DropTable table = new Gson().fromJson(dropTable, DropTable.class);
                        mob.setDropTable(table);

                        return mob;
                    }
                }
                case "blockSpec" -> {
                    String walkable = tag.getString("walkable");
                    BlockSpec spec = new BlockSpec(tag);
                    spec.setWalkable(walkable.equalsIgnoreCase("true"));

                    return spec;
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

                    //registerSpawner to holder...
                    structure.setDescription(current);
                    return structure;
                }

                case "user" -> {
                    String username = tag.getString("username");
                    String uuid = tag.getString("uuid");
                    String passwordHash = tag.getString("password_hash");


                    //regex pattern for username
                    Pattern pattern = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){5,17}[a-zA-Z0-9]$"); //NOTE: USERNAME LENGTH MUST BE 5-20, ONLY CHARACTERS AND NUMBERS
                    Matcher username_match = pattern.matcher(username);

                    Pattern password_ptn = Pattern.compile("^[a-zA-Z0-9](?:[._]?[a-zA-Z0-9]){6,30}[a-zA-Z0-9]$");
                    Matcher pwd_mth = password_ptn.matcher(passwordHash);
                    //regex pattern for uuid
                    Matcher uuid_match = Pattern.compile("^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$").matcher(uuid);
                    if (username_match.matches() && uuid_match.matches() && pwd_mth.matches()) {
                        //matches till here, good to go
                        return new User(username, passwordHash, uuid);
                    } else {
                        throw new IllegalArgumentException("username or password hash is illegal...");
                    }
                }

                case "crafting_recipe" -> {
                    String recipe_name = tag.getString("crafting_recipe_name");
                    ListTag ingredients = tag.getList("ingredients");
                    boolean ordered = tag.getInt("ordered") >= 1;
                    boolean creatable = tag.getInt("craftable") >= 1;
                    String destination_def = tag.getString("destination");
                    if (ingredients.isEmpty()) {
                        throw new IllegalArgumentException("invalid compound ingredients: list 'ingredients' is EMPTY");
                    }

                    Set<CraftingSlot> slots = new HashSet<>();
                    Equipment destination = new Gson().fromJson(destination_def, Equipment.class);
                    if (EquipmentHolder.getEquipments().contains(destination)) {
                        logger.debug("equipment is registered in holder: " + destination_def);
                    } else {
                        logger.debug("equipment is not registered yet, maybe later...");
                    }
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


                    return new CraftingRecipe(destination, recipe_name, ordered, creatable, slots);
                }

                case "equipment" -> {
                    String crafting_recipe_name = tag.getString("recipe_name");
                    String equipment_name = tag.getString("equipment_name");
                    ListTag allowedSlots = tag.getList("slots");
                    for (int i = 0; i <= allowedSlots.size() - 1; i++) {
                        String inventory_slot_name = allowedSlots.getString(i);
                        for (InventoryType a : InventoryType.values()) {
                            if (inventory_slot_name.equals(a.name())) {
                                Equipment equipment = new Equipment(equipment_name);
                                CraftingRecipe recipe = null;
                                for (CraftingRecipe _recipe : CraftingRecipeHolder.getRecipes()) {
                                    if (_recipe.getName().equalsIgnoreCase(crafting_recipe_name)) {
                                        recipe = _recipe;
                                    }
                                }
                                if (recipe == null) {
                                    throw new IllegalArgumentException("fucked...");
                                }

                                return equipment;
                            } else throw new IllegalArgumentException("fucked...");
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage());
        }
        throw new IllegalArgumentException("passed in illegal data.");
    }
}
