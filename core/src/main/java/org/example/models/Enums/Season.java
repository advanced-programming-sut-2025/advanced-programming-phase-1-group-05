package org.example.models.Enums;

public enum Season {
    SPRING("Spring", "tree_spring.png", "tile_summer.png"),
    SUMMER("Summer", "tree.png", "Soil.png"),
    FALL("Fall", "tree_autumn.png", "tile_autumn.png"),
    WINTER("Winter", "tree_snow.png", "tile_winter.png");

    private final String name;
    private final String treeTexture;
    private final String soilTexture;

    Season(String name, String treeTexture, String soilTexture) {
        this.name = name;
        this.treeTexture = treeTexture;
        this.soilTexture = soilTexture;
    }

    public Season next() {
        switch (this) {
            case SPRING:
                return SUMMER;
            case SUMMER:
                return FALL;
            case FALL:
                return WINTER;
            case WINTER:
                return SPRING;
            default:
                return SPRING;
        }
    }

    public String getTreeTexture() {
        return treeTexture;
    }

    public String getSoilTexture() {
        return soilTexture;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Season fromString(String season) {
        for (Season s : Season.values()) {
            if (s.name.equalsIgnoreCase(season)) {
                return s;
            }
        }
        return null;
    }
}
