package org.example.models.Enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ForagingTreeSourceType implements Material {
    Acorns("Acorns", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MapleSeeds("Maple Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    PineCones("Pine Cones", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MahoganySeeds("Mahogany Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MushroomTreeSeeds("Mushroom Tree Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER));

    private final String name;
    private final List<Season> seasons;
    ForagingTreeSourceType(String name, List<Season> seasons) {
        this.name = name;
        this.seasons = seasons;
    }
    public String getName() {
        return name;
    }
    public List<Season> getSeasons() {
        return seasons;
    }
    public static ForagingTreeSourceType fromString(String name){
        for (ForagingTreeSourceType sourceType : ForagingTreeSourceType.values()) {
            if (sourceType.name().replaceAll("\\s", "").equalsIgnoreCase(name)) {
                return sourceType;
            }
        }
        return null;
    }
    public static ForagingTreeSourceType getRandomForagingTreeType (Season currentSeason) {
        Random random = new Random();
        List<ForagingTreeSourceType> foragingTreeTypes = new ArrayList<>();
        for (ForagingTreeSourceType f : ForagingTreeSourceType.values()) {
            if (f.getSeasons().contains(currentSeason)) foragingTreeTypes.add(f);
        }
        while (true){
            int rand = random.nextInt(foragingTreeTypes.size());
            ForagingTreeSourceType f = foragingTreeTypes.get(rand);
            return f;
        }
    }
    @Override
    public int getPrice() {
        return 0;
    }
}
