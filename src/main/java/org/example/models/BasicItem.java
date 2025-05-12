package org.example.models;

import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.Material;

import java.util.Map;

public class BasicItem implements Item {
    private String name;
    private int price;
    private ItemLevel level;
    private Material material;
    public BasicItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public Material getMaterial() {
        return material;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {

    }

    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }
}
