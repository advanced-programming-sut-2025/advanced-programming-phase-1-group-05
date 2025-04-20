package models;

import models.Building.Building;

import java.util.ArrayList;

public class Coop extends Building {
    private final int capacity;
    private final ArrayList<Animal> animals = new ArrayList<>();

    public Coop(int capacity) {
        this.capacity = capacity;
    }

    //for adding an animal to a coop
    public void addAnimal(Animal animal) {
        if(animals.size() < capacity) {
            animals.add(animal);
        }
    }
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }

    public void shepherdAnimal(Animal animal) {
        //player can take animal out of the coop
    }

    public void feedHay(Animal animal) {
        animal.feed();
    }

}
