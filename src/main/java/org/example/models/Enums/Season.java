package org.example.models.Enums;

import java.util.List;

public enum Season {
    SPRING("Spring",List.of(CropType.Cauliflower, CropType.Parsnip, CropType.Potato, CropType.BlueJazz, CropType.Tulip)),
    SUMMER("Summer",List.of(CropType.Corn, CropType.HotPepper, CropType.Radish, CropType.Wheat, CropType.Poppy, CropType.Sunflower, CropType.SummerSpangle)),
    FALL("Fall",List.of(CropType.Artichoke, CropType.Corn, CropType.Eggplant, CropType.Pumpkin, CropType.Sunflower, CropType.FairyRose)),
    WINTER("Winter",List.of(CropType.PowderMelon));

    private final String name;
    private final List<CropType> possibleSeeds;

    Season(String name, List<CropType> possibleSeeds) {
        this.name = name;
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
    @Override
    public String toString() {
        return name;
    }
    public List<CropType> getPossibleSeeds() {
        return possibleSeeds;
    }
}