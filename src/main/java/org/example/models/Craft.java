package org.example.models;

import org.example.models.Enums.CraftType;
import org.example.models.Enums.Material;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Craft implements Item {
    private Map.Entry<Integer,Integer> coordinates;
    private final CraftType type;

    public Craft(CraftType type) {
        this.type = type;
    }

    public Map<Material, Integer> getIngredients() {
        return type.getIngredients();
    }

    public String getSource() {
        return type.getSource();
    }

    public Map.Entry<Integer,Integer> getCoordinates() {
        return coordinates;
    }

    @Override
    public String getName() {
        return type.getName();
    }
    @Override
    public int getPrice() {
        return type.getPrice();
    }
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
        this.coordinates = coordinates;
    }

}
