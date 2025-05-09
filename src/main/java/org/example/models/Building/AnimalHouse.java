package org.example.models.Building;

import org.example.models.Animal;
import org.example.models.Enums.AnimalHouseLevel;
import org.example.models.Enums.AnimalType;
import org.example.models.Enums.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class AnimalHouse extends Building{
    private AnimalType Type; // coop or barn
    private AnimalHouseLevel level;
    private List<Animal> animals = new ArrayList<Animal>();
    private int capacity;

    public AnimalHouse(AnimalType type, AnimalHouseLevel level) {
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
}
