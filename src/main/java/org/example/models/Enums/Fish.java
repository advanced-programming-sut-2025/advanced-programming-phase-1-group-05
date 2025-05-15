package org.example.models.Enums;

import org.example.models.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public enum Fish implements Material, Item {

    Salmon("Salmon",75, Season.FALL, false),
    Sardine("Sardine",40, Season.FALL, false),
    Shad("Shad",60, Season.FALL, false),
    BlueDiscus("Blue Discus",120, Season.FALL, false),
    MidnightCarp("Midnight Carp",150, Season.WINTER, false),
    Squid("Squid",80, Season.WINTER, false),
    Tuna("Tuna",100, Season.WINTER, false),
    Perch("Perch",55, Season.WINTER, false),
    Flounder("Flounder",100, Season.SPRING, false),
    Lionfish("Lion Fish",100, Season.SPRING, false),
    Herring("Herring",30, Season.SPRING, false),
    GhostFish("Ghost Fish",45, Season.SPRING, false),
    Tilapia("Tilapia", 75, Season.SUMMER, false),
    Dorado("Dorado",100, Season.SUMMER, false),
    Sunfish("Sun Fish",30, Season.SUMMER, false),
    RainbowTrout("Rainbow Trout",65, Season.SUMMER, false),
    Legend("Legend",5000, Season.SPRING, true),
    GlacierFish("Glacier Fish",100, Season.WINTER, true),
    Angler("Angler",900, Season.FALL, true),
    CrimsonFish("Crimson Fish",1500, Season.SUMMER, true)
   ;

    private final String name;
    private final int price;
    private final Season season;
    private final boolean legendary;
    Fish(String name, int price, Season season, boolean legendary) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.legendary = legendary;
    }
    private boolean canBeCaught(Season currentSeason, int fishingSkill) {
        if (legendary) return fishingSkill == 4;
        return season.equals(currentSeason);
    }
    public static Fish fromString(String name){
        for (Fish fish : Fish.values()) {
            if (fish.name().replaceAll("\\s", "").equalsIgnoreCase(name)) {
                return fish;
            }
        }
        return null;
    }

    public static Fish getRandomFish(Season currentSeason, int fishingSkill) {
        Random random = new Random();
        List<Fish> fishInSeason = new ArrayList<>();
        for (Fish fish : Fish.values()) {
            if (fish.season == currentSeason) fishInSeason.add(fish);
        }
        int tries = 10;
        while (tries > 0){
            int rand = random.nextInt(fishInSeason.size());
            Fish fish = fishInSeason.get(rand);
            if (fish.canBeCaught(currentSeason, fishingSkill)) return fish;
            tries --;
        }
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
    @Override
    public int getPrice() {
        return price;
    }
}