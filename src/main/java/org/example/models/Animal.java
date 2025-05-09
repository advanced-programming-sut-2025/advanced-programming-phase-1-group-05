package org.example.models;

import org.example.models.Building.AnimalHouse;
import org.example.models.Enums.AnimalType;

public class Animal {
    private String name;
    private AnimalType type;
    private Item product;
    private int health;
    private int hunger;
    private AnimalHouse currentEnclosure;


    public void setCurrentEnclosure(String enclosureName) {}

    public void feed() {
        health += 1;
        hunger -= 1;
    }


}