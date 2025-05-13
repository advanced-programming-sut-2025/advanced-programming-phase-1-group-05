package org.example.models.Enums;

import org.example.models.Item;

import java.util.List;

public class ItemRegistry {
    private static final List<Class<? extends Enum<?>>> itemEnums = List.of(
            CropType.class,
            ForagingTreeSourceType.class,
            ForagingCrop.class,
            ForagingSeedType.class,
            CraftType.class,
            CookingRecipeType.class
    );

    public static Item findItemByName(String name) {
        String upper = name.toUpperCase();

        for (Class<? extends Enum<?>> enumClass : itemEnums) {
            for (Enum<?> constant : enumClass.getEnumConstants()) {
                if (constant.name().equalsIgnoreCase(upper)) {
                    return (Item) constant;
                }
            }
        }

        return null;
    }
}
