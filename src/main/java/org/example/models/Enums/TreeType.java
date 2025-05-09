package org.example.models.Enums;

import java.util.List;

public enum TreeType {
    ApricotTree("Apricot Tree", "Apricot Sapling", "7-7-7-7", 28, "Apricot", 1, 59, true, 38, List.of(Season.SPRING)),
    CherryTree("Cherry Tree", "Cherry Sapling", "7-7-7-7", 28, "Cherry", 1, 80, true, 38, List.of(Season.SPRING)),
    BananaTree("Banana Tree", "Banana Sapling", "7-7-7-7", 28, "Banana", 1, 150, true, 75, List.of(Season.SUMMER)),
    MangoTree("Mango Tree", "Mango Sapling", "7-7-7-7", 28, "Mango", 1, 130, true, 100, List.of(Season.SUMMER)),
    OrangeTree("Orange Tree", "Orange Sapling", "7-7-7-7", 28, "Orange", 1, 100, true, 38, List.of(Season.SUMMER)),
    PeachTree("Peach Tree", "Peach Sapling", "7-7-7-7", 28, "Peach", 1, 140, true, 38, List.of(Season.SUMMER)),
    AppleTree("Apple Tree", "Apple Sapling", "7-7-7-7", 28, "Apple", 1, 100, true, 38, List.of(Season.FALL)),
    PomegranateTree("Pomegranate Tree", "Pomegranate Sapling", "7-7-7-7", 28, "Pomegranate", 1, 140, true, 38, List.of(Season.FALL)),
    OakTree("Oak Tree", "Acorns", "7-7-7-7", 28, "Oak Resin", 7, 150, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MapleTree("Maple Tree", "Maple Seeds", "7-7-7-7", 28, "Maple Syrup", 9, 200, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    PineTree("Pine Tree", "Pine Cones", "7-7-7-7", 28, "Pine Tar", 5, 100, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MahoganyTree("Mahogany Tree", "Mahogany Seeds", "7-7-7-7", 28, "Sap", 1, 2, true, -2, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MushroomTree("Mushroom Tree", "Mushroom Tree Seeds", "7-7-7-7", 28, "Common Mushroom", 1, 40, true, 38, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MysticTree("Mystic Tree", "Mystic Tree Seeds", "7-7-7-7", 28, "Mystic Syrup", 7, 1000, true, 500, List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER));

    private final String name;
    private final String seed;
    private final String stages;
    private final int totalHarvestTime;
    private final String fruit;
    private final int fruitHarvestCycle;
    private final int fruitPrice;
    private final boolean isFruitEdible;
    private final int fruitEnergy;
    private final List<Season> seasons;

    TreeType(String name, String seed, String stages, int totalHarvestTime, String fruit,
             int fruitHarvestCycle, int fruitPrice,boolean isFruitEdible, int fruitEnergy, List<Season> seasons ) {
        this.name = name;
        this.seed = seed;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.fruit = fruit;
        this.fruitHarvestCycle = fruitHarvestCycle;
        this.fruitPrice = fruitPrice;
        this.isFruitEdible = isFruitEdible;
        this.fruitEnergy = fruitEnergy;
        this.seasons = seasons;

    }
    public String getName() {
        return name;
    }
    public String getSeed() {
        return seed;
    }
    public String getStages() {
        return stages;
    }
    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }
    public String getFruit() {
        return fruit;
    }
    public int getFruitHarvestCycle() {
        return fruitHarvestCycle;
    }
    public int getFruitPrice() {
        return fruitPrice;
    }
    public boolean isFruitEdible() {
        return isFruitEdible;
    }
    public int getFruitEnergy() {
        return fruitEnergy;
    }
    public List<Season> getSeasons() {
        return seasons;
    }

}
