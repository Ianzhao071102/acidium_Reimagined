package org.izdevs.acidium.basic;

import com.google.gson.Gson;
import jakarta.persistence.AttributeConverter;
import org.izdevs.acidium.game.inventory.Inventory;

public class PlayerInventoryConverter implements AttributeConverter<Inventory,String> {
    @Override
    public String convertToDatabaseColumn(Inventory inventory) {
        return new Gson().toJson(inventory);
    }

    @Override
    public Inventory convertToEntityAttribute(String s) {
        return new Gson().fromJson(s,Inventory.class);
    }
}
