package org.example.models.Enums;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Item;

import java.util.List;

public enum PossibleSeed implements Material, Item {
    SPRING("Spring",List.of(CropType.Cauliflower, CropType.Parsnip, CropType.Potato, CropType.BlueJazz, CropType.Tulip)),
    SUMMER("Summer",List.of(CropType.Corn, CropType.HotPepper, CropType.Radish, CropType.Wheat, CropType.Poppy, CropType.Sunflower, CropType.SummerSpangle)),
    FALL("Fall",List.of(CropType.Artichoke, CropType.Corn, CropType.Eggplant, CropType.Pumpkin, CropType.Sunflower, CropType.FairyRose)),
    WINTER("Winter",List.of(CropType.PowderMelon));

    private final String name;
    private final List<CropType> possibleSeeds;
    PossibleSeed(String name, List<CropType> possibleSeeds) {
        this.name = name;
        this.possibleSeeds = possibleSeeds;
    }
    public static List<CropType> getPossibleSeeds(Season season) {
        if (season == Season.SPRING) return PossibleSeed.SPRING.possibleSeeds;
        else if (season == Season.SUMMER) return PossibleSeed.SUMMER.possibleSeeds;
        else if (season == Season.FALL) return PossibleSeed.FALL.possibleSeeds;
        else return PossibleSeed.WINTER.possibleSeeds;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return 0;
    }
    public TextureRegion getTexture() {
        return null;
    }
}
