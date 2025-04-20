package models;

import models.Enums.AnimalType;

public class Animal {
    private AnimalType type;
    private String name;
    private int health;
    private int hunger;
    private String currentEnclosure;


    public void setCurrentEnclosure(String enclosureName) {}

    public void feed() {
        health += 1;
        hunger -= 1;
    }
}
