package org.example.models.Enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public enum Fish {

    Salmon(75, Season.FALL, false),
    Sardine(40, Season.FALL, false),
    Shad(60, Season.FALL, false),
    BlueDiscus(120, Season.FALL, false),
    MidnightCarp(150, Season.WINTER, false),
    Squid(80, Season.WINTER, false),
    Tuna(100, Season.WINTER, false),
    Perch(55, Season.WINTER, false),
    Flounder(100, Season.SPRING, false),
    Lionfish(100, Season.SPRING, false),
    Herring(30, Season.SPRING, false),
    GhostFish(45, Season.SPRING, false),
    Tilapia(75, Season.SUMMER, false),
    Dorado(100, Season.SUMMER, false),
    Sunfish(30, Season.SUMMER, false),
    RainbowTrout(65, Season.SUMMER, false),
    Legend(5000, Season.SPRING, true),
    GlacierFish(100, Season.WINTER, true),
    Angler(900, Season.FALL, true),
    CrimsonFish(1500, Season.SUMMER, true)
   ;

    private final int price;
    private final Season season;
    private final boolean legendary;
    Fish(int price, Season season, boolean legendary) {
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
}