package org.example.models.Enums;

import java.util.List;

public enum Season {
    SPRING(List.of("Cauliflower", "Parsnip", "Potato", "Blue Jazz", "Tulip")),
    SUMMER(List.of("Corn", "Hot Pepper", "Radish", "Wheat", "Poppy", "Sunflower", "Summer Spangle")),
    FALL(List.of("Artichoke", "Corn", "Eggplant", "Pumpkin", "Sunflower", "Fairy Rose")),
    WINTER(List.of("Powdermelon"));

    private final List<String> possibleSeeds;

    Season(List<String> possibleSeeds) {
        this.possibleSeeds = possibleSeeds;
    }
    public Season next() {
        return switch (this){
            case SPRING ->  SUMMER;
            case SUMMER ->  FALL;
            case FALL ->  WINTER;
            default ->  SPRING;
        };
    }
}