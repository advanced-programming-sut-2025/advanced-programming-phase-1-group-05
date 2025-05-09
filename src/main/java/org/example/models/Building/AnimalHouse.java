package org.example.models.Building;

import org.example.models.Animal;
import org.example.models.Enums.AnimalHouseLevel;
import org.example.models.Enums.EnclosureType;
import org.example.models.Enums.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class AnimalHouse extends Building{
    private EnclosureType Type; // coop or barn
    private AnimalHouseLevel level;
    private List<Animal> animals = new ArrayList<Animal>();
    private int capacity;

    public AnimalHouse(EnclosureType type, AnimalHouseLevel level) {
        this.Type = type;
        this.level = level;
        this.capacity = level.getCapacity();
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
    }

    public BuildingType getType() {
        return BuildingType.valueOf(Type.name());
    }

    public List<Animal> getAnimals () {
        return animals;
    }
    public Animal getAnimal(String name) {
        for (Animal animal : animals) {
            if (animal.getName().equalsIgnoreCase(name))
                return animal;
        }
        return null;
    }
}
