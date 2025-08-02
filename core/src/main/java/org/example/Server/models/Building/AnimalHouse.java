package org.example.Server.models.Building;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.Server.models.Animal;
import org.example.Common.Enums.AnimalHouseLevel;
import org.example.Common.Enums.EnclosureType;
import org.example.Common.Enums.BuildingType;

import java.util.ArrayList;
import java.util.List;

public class AnimalHouse extends Building{
    private EnclosureType Type; // coop or barn
    private AnimalHouseLevel level;
    private List<Animal> animals = new ArrayList<Animal>();
    private int capacity;
    private Texture texture;
    float x, y;
    public AnimalHouse(EnclosureType type, AnimalHouseLevel level, float x, float y, Texture texture) {
        this.Type = type;
        this.level = level;
        this.capacity = level.getCapacity();
        this.texture = texture;
        this.x = x;
        this.y = y;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);

    }

    public int getCapacity() {
        return capacity;
    }
    public boolean contains(Animal animal) {
        return animals.contains(animal);
    }
    public void removeAnimal(Animal animal) {
        animals.remove(animal);
    }
    public BuildingType getType() {
        return BuildingType.fromString(Type.name());
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

    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, x, y);
    }}
