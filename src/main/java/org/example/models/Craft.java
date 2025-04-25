package org.example.models;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Craft implements Item {
    private Map.Entry<Integer,Integer> coordinates;
    private String name;
    private int price;
    private List<Item> ingredients;
    private String source;
    public Craft(int price, String name, List<Item> ingredients, String source) {
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.source = source;
    }

    public List<Item> getIngredients() {
        return ingredients;
    }

    public String getSource() {
        return source;
    }

    public Map.Entry<Integer,Integer> getCoordinates() {
        return coordinates;
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
        this.coordinates = coordinates;
    }

}
