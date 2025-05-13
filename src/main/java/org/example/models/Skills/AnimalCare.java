package org.example.models.Skills;

import org.example.models.Animal;
import org.example.models.Enums.ItemLevel;
import org.example.models.GameTile;

import java.util.List;
import java.util.Map;

public class AnimalCare implements Skill{
    int level = 0;
    int capacity = 0;
    private Map<String, List<Animal>> enclosures;

    public void buildEnclosure(String enclosureName, String type, int capacity) {}

    public void addAnimalToEnclosure(Animal animal, String enclosureName) {}

    public void feedAnimal(Animal animal, String foodType) {}

    public void sellAnimal(Animal animal) {}

    public String collectProduct(Animal animal) { return null; }
    public void milkAnimal(Animal animal) {
        //milk the animal on the tile
    }
    public void shaveAnimal(Animal animal) {
        //shave sheep
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public void increaseLevel() {
        if(level < 4) this.level ++;
    }
    @Override
    public boolean canGoToNextLevel() {
        if((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1)*100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 5;
    }

    @Override
    public boolean isMaxLevel() {
        return level == 10;
    }
    @Override
    public void handleLevelChangeTrophies(int level) {

    }

}