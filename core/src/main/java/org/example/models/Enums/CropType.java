package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Item;

import java.util.List;

public enum CropType implements Material, Item {
    BlueJazz("Blue Jazz", "Jazz Seeds", "1-2-2-2", 7, true, 0, 50, true, 45, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Blue_Jazz.png"),
    Carrot("Carrot", "Carrot Seeds", "1-1-1", 3, true, 0, 35, true, 75, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Carrot.png"),
    Cauliflower("Cauliflower", "Cauliflower Seeds", "1-2-4-4-1", 12, true, 0, 175, true, 75, List.of(Season.SPRING), true,"Stardew_Valley_Images-main/Farming/Cauliflower.png"),
    CoffeeBean("Coffee Bean", "Coffee Bean", "1-2-2-3-2", 12, false, 2, 15, false, 0, List.of(Season.SPRING, Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Coffee_Bean.png"),
    Garlic("Garlic", "Garlic Seeds", "1-1-1-1", 4, true, 0, 60, true, 20, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Garlic.png"),
    GreenBean("Green Bean", "Bean Starter", "1-1-3-4", 10, false, 3, 40, true, 25, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Green_Bean.png"),
    Kale("Kale", "Kale Seeds", "1-2-2-1", 6, true, 0, 110, true, 50, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Kale.png"),
    Parsnip("Parsnip", "Parsnip Seeds", "1-1-1-1", 4, true, 0, 35, true, 25, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Parsnip.png"),
    Potato("Potato", "Potato Seeds", "1-1-1-2-1", 6, true, 0, 80, true, 25, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Potato.png"),
    Rhubarb("Rhubarb", "Rhubarb Seeds", "2-2-2-3-4", 13, true, 0, 220, false, 0, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Rhubarb.png"),
    Strawberry("Strawberry", "Strawberry Seeds", "1-1-2-2-2", 8, false, 4, 120, true, 50, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Strawberry.png"),
    Tulip("Tulip", "Tulip Bulb", "1-1-2-2", 6, true, 0, 30, true, 45, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Tulip.png"),
    UnmilledRice("Unmilled Rice", "Rice Shoot", "1-2-2-3", 8, true, 0, 30, true, 3, List.of(Season.SPRING), false,"Stardew_Valley_Images-main/Farming/Unmilled_Rice.png"),
    Blueberry("Blueberry", "Blueberry Seeds", "1-3-3-4-2", 13, false, 4, 50, true, 25, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Blueberry.png"),
    Corn("Corn", "Corn Seeds", "2-3-3-3-3", 14, false, 4, 50, true, 25, List.of(Season.SUMMER, Season.FALL), false,"Stardew_Valley_Images-main/Farming/Corn.png"),
    Hops("Hops", "Hops Starter", "1-1-2-3-4", 11, false, 1, 25, true, 45, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Hops.png"),
    HotPepper("Hot Pepper", "Pepper Seeds", "1-1-1-1-1", 5, false, 3, 40, true, 13, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Hot_Pepper.png"),
    Melon("Melon", "Melon Seeds", "1-2-3-3-3", 12, true, 0, 250, true, 113, List.of(Season.SUMMER), true,"Stardew_Valley_Images-main/Farming/Melon.png"),
    Poppy("Poppy", "Poppy Seeds", "1-2-2-2", 7, true, 0, 140, true, 45, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Poppy.png"),
    Radish("Radish", "Radish Seeds", "2-1-2-1", 6, true, 0, 90, true, 45, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Radish.png"),
    RedCabbage("Red Cabbage", "Red Cabbage Seeds", "2-1-2-2-2", 9, true, 0, 260, true, 75, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Red_Cabbage.png"),
    StarFruit("StarFruit", "Startfruit Seeds", "2-3-2-3-3", 13, true, 0, 750, true, 125, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Starfruit.png"),
    SummerSpangle("Summer Spangle", "Spangle Seeds", "1-2-3-1", 8, true, 0, 90, true, 45, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Summer_Spangle.png"),
    SummerSquash("Summer Squash", "Summer Squash Seeds", "1-1-1-2-1", 6, false, 3, 45, true, 63, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Summer_Squash.png"),
    Sunflower("Sunflower", "Sunflower Seeds", "1-2-3-2", 8, true, 0, 80, true, 45, List.of(Season.SUMMER, Season.FALL), false,"Stardew_Valley_Images-main/Farming/Sunflower.png"),
    Tomato("Tomato", "Tomato Seeds", "2-2-2-2-3", 11, false, 4, 60, true, 20, List.of(Season.SUMMER), false,"Stardew_Valley_Images-main/Farming/Tomato.png"),
    Wheat("Wheat", "Wheat Seeds", "1-1-1-1", 4, true, 0, 25, false, 0, List.of(Season.SUMMER, Season.FALL), false,"Stardew_Valley_Images-main/Farming/Wheat.png"),
    Amaranth("Amaranth", "Amaranth Seeds", "1-2-2-2", 7, true, 0, 150, true, 50, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Amaranth.png"),
    Artichoke("Artichoke", "Artichoke Seeds", "2-2-1-2-1", 8, true, 0, 160, true, 30, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Artichoke.png"),
    Beet("Beet", "Beet Seeds", "1-1-2-2", 6, true, 0, 100, true, 30, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Beet.png"),
    BokChoy("Bok Choy", "Bok Choy Seeds", "1-1-1-1", 4, true, 0, 80, true, 25, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Bok_Choy.png"),
    Broccoli("Broccoli", "Broccoli Seeds", "2-2-2-2", 8, false, 4, 70, true, 63, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Broccoli.png"),
    Cranberries("Cranberries", "Cranberry Seeds", "1-2-1-1-2", 7, false, 5, 75, true, 38, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Cranberries.png"),
    Eggplant("Eggplant", "Eggplant Seeds", "1-1-1-1", 5, false, 5, 60, true, 20, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Eggplant.png"),
    FairyRose("Fairy Rose", "Fairy Seeds", "1-4-4-3", 12, true, 0, 290, true, 45, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Fairy_Rose.png"),
    Grape("Grape", "Grape Starter", "1-1-2-3-3", 10, false, 3, 80, true, 38, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Grape.png"),
    Pumpkin("Pumpkin", "Pumpkin Seeds", "1-2-3-4-2", 13, true, 0, 320, false, 0, List.of(Season.FALL), true,"Stardew_Valley_Images-main/Farming/Pumpkin.png"),
    Yam("Yam", "Yam Seeds", "1-3-3-3", 10, true, 0, 160, true, 45, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Yam.png"),
    SweetGemBerry("Sweet Gem Berry", "Rare Seed", "2-4-6-6-6", 24, true, 0, 3000, false, 0, List.of(Season.FALL), false,"Stardew_Valley_Images-main/Farming/Sweet_Gem_Berry.png"),
    PowderMelon("Powdermelon", "Powdermelon Seeds", "1-2-1-2-1", 7, true, 0, 60, true, 63, List.of(Season.WINTER), true,"Stardew_Valley_Images-main/Farming/Powdermelon.png"),
    AncientFruit("Ancient Fruit", "Ancient Seeds", "2-7-7-7-5", 28, false, 7, 550, false, 0, List.of(Season.SPRING, Season.SUMMER, Season.FALL), false,"Stardew_Valley_Images-main/Farming/Ancient_Fruit.png");

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
    private final String texturePath;
    CropType(String name, String source, String stages, int totalHarvestTime, boolean oneTime,
             int regrowthTime, int price, boolean isEdible, int energy, List<Season> seasons, boolean canBeGiant, String texturePath) {
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
        this.texturePath = texturePath;

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

    public static CropType getSeedType(String seed){
        for(CropType cropType : CropType.values()){
            if(cropType.source.contains(seed)){
                return cropType;
            }
        }
        return null;
    }
    public static CropType fromString(String name){
        for (CropType cropType : CropType.values()) {
            if (cropType.getName().equals(name)) {
                return cropType;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name :").append(name).append("\n").append("Source :").
                append(source).append("\n").append("Stages :").append(stages).
                append("\n").append("Total Harvest Time :").append(totalHarvestTime).
                append("\n").append("One Time :").append(oneTime).append("\n").
                append("Regrowth Time :").append(regrowthTime).append("\n").
                append("Base Sell Price :").append(price).append("\n").append("Is Edible :").
                append(isEdible).append("\n").append("Base Energy :").append(energy).append("\n").
                append("Season :");
        for(Season season : seasons){
            builder.append(season).append(",");
        }
        builder.deleteCharAt(builder.length()-1);
        builder.append("\n" + "Can Become Giant :").append(canBecomeGiant);
        return builder.toString();
    }
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(this.texturePath));
    }
}
