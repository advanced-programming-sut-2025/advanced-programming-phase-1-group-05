package models.Building;

import models.FruitAndVegetable;

import java.util.List;

public class GreenHouse {
    private List<FruitAndVegetable> fruitAndVegetables;

    public void addPlant(FruitAndVegetable fruitAndVegetable, int x, int y) {
        fruitAndVegetables.add(fruitAndVegetable);
    }

}
