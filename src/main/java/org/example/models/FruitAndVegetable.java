package org.example.models;

import org.example.models.Enums.GrowthStage;
import org.example.models.Enums.Season;
import org.example.models.Enums.Seed;

public class FruitAndVegetable implements Item {
    String name;
    int price;

    Seed seed;
    GrowthStage growthStage;
    int totalHarvestTime;
    boolean oneTime;
    boolean isEdible;
    int energy;
    Season season;
    boolean isGiantifiable; //change name

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }
}