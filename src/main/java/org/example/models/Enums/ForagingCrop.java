package org.example.models.Enums;

import org.example.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ForagingCrop implements Material, Item {
    CommonMushroom("Common Mushroom", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 40, 38),
    Daffodil("Daffodil", List.of(Season.SPRING), 30, 0),
    Leek("Leek", List.of(Season.SPRING), 60, 40),
    Morel("Morel", List.of(Season.SPRING), 150, 20),
    SalmonBerry("Salmonberry", List.of(Season.SPRING), 5, 25),
    SpringOnion("Spring Onion", List.of(Season.SPRING), 8, 13),
    WildHorseRadish("Wild Horseradish", List.of(Season.SPRING), 50, 13),
    FiddleHeadFern("Fiddlehead Fern", List.of(Season.SUMMER), 90, 25),
    Grape("Grape", List.of(Season.SUMMER), 80, 38),
    RedMushroom("Red Mushroom", List.of(Season.SUMMER), 75, -50),
    SpiceBerry("Spice Berry", List.of(Season.SUMMER), 80, 25),
    SweetPea("Sweet Pea", List.of(Season.SUMMER), 50, 0),
    BlackBerry("Blackberry", List.of(Season.FALL), 25, 25),
    Chanterelle("Chanterelle", List.of(Season.FALL), 160, 75),
    Hazelnut("Hazelnut", List.of(Season.FALL), 40, 38),
    PurpleMushroom("Purple Mushroom", List.of(Season.FALL), 90, 30),
    WildPlum("Wild Plum", List.of(Season.FALL), 80, 25),
    Crocus("Crocus", List.of(Season.WINTER), 60, 0),
    CrystalFruit("Crystal Fruit", List.of(Season.WINTER), 150, 63),
    Holly("Holly", List.of(Season.WINTER), 80, -37),
    SnowYam("Snow Yam", List.of(Season.WINTER), 100, 30),
    WinterRoot("Winter Root", List.of(Season.WINTER), 70, 25),
    Apricot("Apricot", List.of(Season.SPRING), 59, 38),
    Cherry("Cherry", List.of(Season.SPRING), 80, 38),
    Banana("Banana", List.of(Season.SUMMER), 150, 75),
    Mango("Mango", List.of(Season.SUMMER), 130, 100),
    Orange("Orange", List.of(Season.SUMMER), 100, 38),
    Peach("Peach", List.of(Season.SUMMER), 140, 38),
    Apple("Apple", List.of(Season.FALL), 100, 38),
    Pomegranate("Pomegranate", List.of(Season.FALL), 140, 38),
    OakResin("Oak Resin", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 150, 0),
    MapleSyrup("Maple Syrup", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 200, 0),
    Sap("Sap", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 2, -2),
    MysticSyrup("Mystic Syrup", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 1000, 500);

    private final String name;
    private final List<Season> seasons;
    private final int price;
    private final int energy;
    ForagingCrop(String name, List<Season> seasons, int price, int energy) {
        this.name = name;
        this.seasons = seasons;
        this.price = price;
        this.energy = energy;
    }

    public String getName() {
        return name;
    }
    public boolean isAvailable(Season season) {
        return seasons.contains(season);
    }
    public List<Season> getSeasons() {
        return seasons;
    }
    public int getPrice() {
        return price;
    }
    public int getEnergy() {
        return energy;
    }

    public static ForagingCrop fromString(String name){
        for (ForagingCrop cropType : ForagingCrop.values()) {
            if (cropType.getName().equals(name)) {
                return cropType;
            }
        }
        return null;
    }
    public static ForagingCrop getRandomForagingCrop (Season currentSeason) {
        Random random = new Random();
        List<ForagingCrop> foragingTypes = new ArrayList<>();
        for (ForagingCrop f : ForagingCrop.values()) {
            if (f.getSeasons().contains(currentSeason)) foragingTypes.add(f);
        }
        while (true){
            int rand = random.nextInt(foragingTypes.size());
            ForagingCrop f = foragingTypes.get(rand);
            return f;
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name : ").append(name).append("\n")
                .append("Price : ").append(price).append("\n")
                .append("Energy : ").append(energy).append("\n")
                .append("Seasons : ");
        for (Season season : seasons) {
            builder.append(season).append(", ");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
