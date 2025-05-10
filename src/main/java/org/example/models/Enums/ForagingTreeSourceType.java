package org.example.models.Enums;

import java.util.List;

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
}
