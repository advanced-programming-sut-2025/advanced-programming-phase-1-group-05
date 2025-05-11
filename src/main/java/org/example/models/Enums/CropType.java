package org.example.models.Enums;

import java.util.List;

public enum CropType implements Material {
    BlueJazz("Blue Jazz", "Jazz Seeds", "1-2-2-2", 7, true, 0, 50, true, 45, List.of(Season.SPRING), false),
    Carrot("Carrot", "Carrot Seeds", "1-1-1", 3, true, 0, 35, true, 75, List.of(Season.SPRING), false),
    Cauliflower("Cauliflower", "Cauliflower Seeds", "1-2-4-4-1", 12, true, 0, 175, true, 75, List.of(Season.SPRING), true),
    CoffeeBean("Coffee Bean", "Coffee Bean", "1-2-2-3-2", 12, false, 2, 15, false, 0, List.of(Season.SPRING, Season.SUMMER), false),
    Garlic("Garlic", "Garlic Seeds", "1-1-1-1", 4, true, 0, 60, true, 20, List.of(Season.SPRING), false),
    GreenBean("Green Bean", "Bean Starter", "1-1-3-4", 10, false, 3, 40, true, 25, List.of(Season.SPRING), false),
    Kale("Kale", "Kale Seeds", "1-2-2-1", 6, true, 0, 110, true, 50, List.of(Season.SPRING), false),
    Parsnip("Parsnip", "Parsnip Seeds", "1-1-1-1", 4, true, 0, 35, true, 25, List.of(Season.SPRING), false),
    Potato("Potato", "Potato Seeds", "1-1-1-2-1", 6, true, 0, 80, true, 25, List.of(Season.SPRING), false),
    Rhubarb("Rhubarb", "Rhubarb Seeds", "2-2-2-3-4", 13, true, 0, 220, false, 0, List.of(Season.SPRING), false),
    Strawberry("Strawberry", "Strawberry Seeds", "1-1-2-2-2", 8, false, 4, 120, true, 50, List.of(Season.SPRING), false),
    Tulip("Tulip", "Tulip Bulb", "1-1-2-2", 6, true, 0, 30, true, 45, List.of(Season.SPRING), false),
    UnmilledRice("Unmilled Rice", "Rice Shoot", "1-2-2-3", 8, true, 0, 30, true, 3, List.of(Season.SPRING), false),
    Blueberry("Blueberry", "Blueberry Seeds", "1-3-3-4-2", 13, false, 4, 50, true, 25, List.of(Season.SUMMER), false),
    Corn("Corn", "Corn Seeds", "2-3-3-3-3", 14, false, 4, 50, true, 25, List.of(Season.SUMMER, Season.FALL), false),
    Hops("Hops", "Hops Starter", "1-1-2-3-4", 11, false, 1, 25, true, 45, List.of(Season.SUMMER), false),
    HotPepper("Hot Pepper", "Pepper Seeds", "1-1-1-1-1", 5, false, 3, 40, true, 13, List.of(Season.SUMMER), false),
    Melon("Melon", "Melon Seeds", "1-2-3-3-3", 12, true, 0, 250, true, 113, List.of(Season.SUMMER), true),
    Poppy("Poppy", "Poppy Seeds", "1-2-2-2", 7, true, 0, 140, true, 45, List.of(Season.SUMMER), false),
    Radish("Radish", "Radish Seeds", "2-1-2-1", 6, true, 0, 90, true, 45, List.of(Season.SUMMER), false),
    RedCabbage("Red Cabbage", "Red Cabbage Seeds", "2-1-2-2-2", 9, true, 0, 260, true, 75, List.of(Season.SUMMER), false),
    StarFruit("StarFruit", "Startfruit Seeds", "2-3-2-3-3", 13, true, 0, 750, true, 125, List.of(Season.SUMMER), false),
    SummerSpangle("Summer Spangle", "Spangle Seeds", "1-2-3-1", 8, true, 0, 90, true, 45, List.of(Season.SUMMER), false),
    SummerSquash("Summer Squash", "Summer Squash Seeds", "1-1-1-2-1", 6, false, 3, 45, true, 63, List.of(Season.SUMMER), false),
    Sunflower("Sunflower", "Sunflower Seeds", "1-2-3-2", 8, true, 0, 80, true, 45, List.of(Season.SUMMER, Season.FALL), false),
    Tomato("Tomato", "Tomato Seeds", "2-2-2-2-3", 11, false, 4, 60, true, 20, List.of(Season.SUMMER), false),
    Wheat("Wheat", "Wheat Seeds", "1-1-1-1", 4, true, 0, 25, false, 0, List.of(Season.SUMMER, Season.FALL), false),
    Amaranth("Amaranth", "Amaranth Seeds", "1-2-2-2", 7, true, 0, 150, true, 50, List.of(Season.FALL), false),
    Artichoke("Artichoke", "Artichoke Seeds", "2-2-1-2-1", 8, true, 0, 160, true, 30, List.of(Season.FALL), false),
    Beet("Beet", "Beet Seeds", "1-1-2-2", 6, true, 0, 100, true, 30, List.of(Season.FALL), false),
    BokChoy("Bok Choy", "Bok Choy Seeds", "1-1-1-1", 4, true, 0, 80, true, 25, List.of(Season.FALL), false),
    Broccoli("Broccoli", "Broccoli Seeds", "2-2-2-2", 8, false, 4, 70, true, 63, List.of(Season.FALL), false),
    Cranberries("Cranberries", "Cranberry Seeds", "1-2-1-1-2", 7, false, 5, 75, true, 38, List.of(Season.FALL), false),
    Eggplant("Eggplant", "Eggplant Seeds", "1-1-1-1", 5, false, 5, 60, true, 20, List.of(Season.FALL), false),
    FairyRose("Fairy Rose", "Fairy Seeds", "1-4-4-3", 12, true, 0, 290, true, 45, List.of(Season.FALL), false),
    Grape("Grape", "Grape Starter", "1-1-2-3-3", 10, false, 3, 80, true, 38, List.of(Season.FALL), false),
    Pumpkin("Pumpkin", "Pumpkin Seeds", "1-2-3-4-2", 13, true, 0, 320, false, 0, List.of(Season.FALL), true),
    Yam("Yam", "Yam Seeds", "1-3-3-3", 10, true, 0, 160, true, 45, List.of(Season.FALL), false),
    SweetGemBerry("Sweet Gem Berry", "Rare Seed", "2-4-6-6-6", 24, true, 0, 3000, false, 0, List.of(Season.FALL), false),
    PowderMelon("Powdermelon", "Powdermelon Seeds", "1-2-1-2-1", 7, true, 0, 60, true, 63, List.of(Season.WINTER), true),
    AncientFruit("Ancient Fruit", "Ancient Seeds", "2-7-7-7-5", 28, false, 7, 550, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL), false);

    private final String name;
    private final String source;
    private final String stages;
    private final int totalHarvestTime;
    private final boolean oneTime;
    private final int regrowthTime;
    private final int price;
    private final boolean isEdible;
    private final int energy;
    private final List<Season> seasons;
    private final boolean canBecomeGiant;
    CropType(String name, String source, String stages, int totalHarvestTime, boolean oneTime,
             int regrowthTime, int price, boolean isEdible, int energy, List<Season> seasons, boolean canBeGiant) {
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.price = price;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
        this.canBecomeGiant = canBeGiant;

    }
    @Override
    public String getName() {
        return name;
    }
    public String getSource() {
        return source;
    }
    public String getStages() {
        return stages;
    }
    public int getTotalHarvestTime() {
        return totalHarvestTime;
    }
    public boolean isOneTime() {
        return oneTime;
    }
    public int getRegrowthTime() {
        return regrowthTime;
    }
    public int getPrice() {
        return price;
    }
    public boolean isEdible() {
        return isEdible;
    }
    public int getEnergy() {
        return energy;
    }
    public List<Season> getSeasons() {
        return seasons;
    }
    public boolean canBecomeGiant() {
        return canBecomeGiant;
    }

    public static CropType fromString(String name){
        for (CropType cropType : CropType.values()) {
            if (cropType.name().replaceAll("\\s", "").equalsIgnoreCase(name)) {
                return cropType;
            }
        }
        return null;
    }

}
