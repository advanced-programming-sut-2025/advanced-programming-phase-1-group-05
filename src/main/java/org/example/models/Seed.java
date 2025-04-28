package org.example.models;

import java.util.Map;

public class Seed implements Item{

    String name;
    int price;

    public Seed(String name){
        this.name = name;
    }

    public void setPrice(int price){
        this.price = price;
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
