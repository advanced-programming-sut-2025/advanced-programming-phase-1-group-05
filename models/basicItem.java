package models;

import models.Enums.ItemLevel;

public class basicItem implements Item {
    private String name;
    private int price;
    private ItemLevel level;
    public basicItem(String name, int price) {
        this.name = name;
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