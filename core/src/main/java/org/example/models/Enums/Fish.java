package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Item;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public enum Fish implements Material, Item {

    Salmon("Salmon",75, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Salmon.png"),
    Sardine("Sardine",40, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Sardine.png"),
    Shad("Shad",60, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Shad.png"),
    BlueDiscus("Blue Discus",120, Season.FALL, false,"Stardew_Valley_Images-main/Fish/Blue_Discus.png"),
    MidnightCarp("Midnight Carp",150, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Midnight_Carp.png"),
    Squid("Squid",80, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Squid.png"),
    Tuna("Tuna",100, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Tuna.png"),
    Perch("Perch",55, Season.WINTER, false,"Stardew_Valley_Images-main/Fish/Perch.png"),
    Flounder("Flounder",100, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Flounder.png"),
    Lionfish("Lion Fish",100, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Lionfish.png"),
    Herring("Herring",30, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Herring.png"),
    GhostFish("Ghost Fish",45, Season.SPRING, false,"Stardew_Valley_Images-main/Fish/Ghostfish.png"),
    Tilapia("Tilapia", 75, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Tilapia.png"),
    Dorado("Dorado",100, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Dorado.png"),
    Sunfish("Sun Fish",30, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Sunfish.png"),
    RainbowTrout("Rainbow Trout",65, Season.SUMMER, false,"Stardew_Valley_Images-main/Fish/Rainbow_Trout.png"),
    Legend("Legend",5000, Season.SPRING, true,"Stardew_Valley_Images-main/Fish/Legend.png"),
    GlacierFish("Glacier Fish",100, Season.WINTER, true,"Stardew_Valley_Images-main/Fish/Glacierfish.png"),
    Angler("Angler",900, Season.FALL, true,"Stardew_Valley_Images-main/Fish/Angler.png"),
    CrimsonFish("Crimson Fish",1500, Season.SUMMER, true,"Stardew_Valley_Images-main/Fish/Crimsonfish.png")
   ;

    private final String name;
    private final int price;
    private final Season season;
    private final boolean legendary;
    private final String texturePath;
    Fish(String name, int price, Season season, boolean legendary, String texturePath) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.legendary = legendary;
        this.texturePath = texturePath;
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
    public TextureRegion getTexture(){
        return new TextureRegion(new Texture(this.texturePath));
    }
}
