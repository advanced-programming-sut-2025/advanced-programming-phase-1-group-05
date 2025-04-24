package org.example.models;

public class Seed extends Item{
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
}
