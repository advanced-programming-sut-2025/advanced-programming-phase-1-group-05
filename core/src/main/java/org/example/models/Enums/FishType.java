package org.example.models.Enums;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.GameAssetManager;
import org.example.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum FishType implements Material, Item {
    Salmon("Salmon",75, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Salmon.png", FishBehavior.MIXED),
    Sardine("Sardine",40, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Sardine.png", FishBehavior.DART),
    Shad("Shad",60, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Shad.png", FishBehavior.SMOOTH),
    BlueDiscus("Blue Discus",120, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Blue_Discus.png", FishBehavior.DART),
    MidnightCarp("Midnight Carp",150, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Midnight_Carp.png", FishBehavior.MIXED),
    Squid("Squid",80, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Squid.png", FishBehavior.SINKER),
    Tuna("Tuna",100, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Tuna.png", FishBehavior.SMOOTH),
    Perch("Perch",55, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Perch.png", FishBehavior.DART),
    Flounder("Flounder",100, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Flounder.png", FishBehavior.SINKER),
    Lionfish("Lion Fish",100, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Lionfish.png", FishBehavior.SMOOTH),
    Herring("Herring",30, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Herring.png", FishBehavior.DART),
    GhostFish("Ghost Fish",45, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Ghostfish.png", FishBehavior.MIXED),
    Tilapia("Tilapia", 75, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Tilapia.png", FishBehavior.MIXED),
    Dorado("Dorado",100, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Dorado.png", FishBehavior.MIXED),
    Sunfish("Sun Fish",30, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Sunfish.png", FishBehavior.MIXED),
    RainbowTrout("Rainbow Trout",65, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Rainbow_Trout.png", FishBehavior.MIXED),
    Legend("Legend",5000, Season.SPRING, true,"Animals/Fish/Legend.png", FishBehavior.MIXED),
    GlacierFish("Glacier Fish",100, Season.WINTER, true,"Animals/Fish/GlacierFish.png", FishBehavior.MIXED),
    Angler("Angler",900, Season.FALL, true,"Animals/Fish/Angler.png", FishBehavior.SMOOTH),
    CrimsonFish("Crimson Fish",1500, Season.SUMMER, true,"Animals/Fish/CrimsonFish.png", FishBehavior.MIXED)
   ;
    public enum FishBehavior {
        MIXED, SMOOTH, SINKER, FLOATER, DART
    }

    private final String name;
    private final int price;
    private final Season season;
    private final boolean legendary;
    private final String texturePath;
    private final FishBehavior behavior;
    FishType(String name, int price, Season season, boolean legendary, String texturePath, FishBehavior behavior) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.legendary = legendary;
        this.texturePath = texturePath;
        this.behavior = behavior;
    }
    private boolean canBeCaught(Season currentSeason, int fishingSkill) {
        if (legendary) return fishingSkill == 4;
        return season.equals(currentSeason);
    }
    public static FishType fromString(String name){
        for (FishType fish : FishType.values()) {
            if (fish.name().replaceAll("\\s", "").equalsIgnoreCase(name)) {
                return fish;
            }
        }
        return null;
    }

    public static FishType getRandomFish(Season currentSeason, int fishingSkill) {
        Random random = new Random();
        List<FishType> fishInSeason = new ArrayList<>();
        for (FishType fish : FishType.values()) {
            if (fish.season == currentSeason) fishInSeason.add(fish);
        }
        int tries = 10;
        while (tries > 0){
            int rand = random.nextInt(fishInSeason.size());
            FishType fish = fishInSeason.get(rand);
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
    public TextureRegion getTexture(){
        return new TextureRegion(GameAssetManager.getInstance().getOrLoadTexture(this.texturePath));
    }

    public boolean isLegendary() {
        return legendary;
    }

    public FishBehavior getBehavior() {
        return behavior;
    }
}
