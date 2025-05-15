package org.example.models.Enums;

import org.example.models.Game;
import org.example.models.Item;

import java.util.List;

public class ItemRegistry {
    private static final List<Class<? extends Enum<?>>> itemEnums = List.of(
            CropType.class,
            ForagingTreeSourceType.class,
            ForagingCrop.class,
            ForagingSeedType.class,
            CraftType.class,
            CookingRecipeType.class,
            Fish.class,
            MineralType.class
    );

    public static Item findItemByName(String name) {
        if (name == null) return null;

        Item fromDB = Game.getDatabase().getItem(name);
        if (fromDB != null) return fromDB;

        String upper = name.toUpperCase();

        for (Class<? extends Enum<?>> enumClass : itemEnums) {
            Enum<?>[] constants = enumClass.getEnumConstants();
            if (constants == null) continue;

            for (Enum<?> constant : constants) {
                if (constant.name().equalsIgnoreCase(upper) && constant instanceof Item item) {
                    return item;
                }
            }
        }

        return null;
    }

}
