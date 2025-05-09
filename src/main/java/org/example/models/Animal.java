package org.example.models;

import org.example.models.Enums.AnimalType;
import org.example.models.Enums.EnclosureType;

import java.util.ArrayList;
import java.util.List;

public class Animal {
    private String name;
    private EnclosureType enclosureType;
    private List<Item> products = new ArrayList<>();
    private List<Product> unCollectedProducts = new ArrayList<>();
    private int health;
    private int hunger;
    private AnimalType type;

    public Animal(String name, AnimalType type) {
        this.name = name;
        this.type = type;
        initializeAnimal();
    }


    public void setCurrentEnclosure(String enclosureName) {}

    public void feed() {
        health += 1;
        hunger -= 1;
    }

    public String getName() {
        return name;
    }

    private void initializeAnimal() {
        switch (type) {
            case COW -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Milk"));
                products.add(Game.getDatabase().getItem("Large milk"));
            }
            case GOAT -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Goat Milk"));
                products.add(Game.getDatabase().getItem("Large goat milk"));
            }
            case PIG -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Truffle"));
            }
            case DUCK -> {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Duck feather"));
                products.add(Game.getDatabase().getItem("Duck egg"));
            }
            case RABBIT -> {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Wool"));
                products.add(Game.getDatabase().getItem("Rabbit's foot"));
            }
            case SHEEP -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Wool"));
            }
            case CHICKEN -> {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Egg"));
                products.add(Game.getDatabase().getItem("Large egg"));
            }

        }
    }

    public List<Product> getUnCollectedProducts() {
        return unCollectedProducts;
    }
}