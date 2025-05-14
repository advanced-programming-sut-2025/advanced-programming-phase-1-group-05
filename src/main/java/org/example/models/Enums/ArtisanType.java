package org.example.models.Enums;

import org.example.models.ArtisanProduct;
import org.example.models.Game;
import org.example.models.Item;
import org.example.models.Product;

import java.util.List;
import java.util.Map;

public enum ArtisanType {
    BeeHouse(CraftType.BeeHouse, List.of(Map.of(Map.of(), new ArtisanProduct("Honey", 75, 96, 350)))),
    CheesePress(CraftType.CheesePress, List.of(Map.of(Map.of(Game.getDatabase().getItem("milk"), 1), new ArtisanProduct("cheese", 100, 3, 230)),
            Map.of(Map.of(Game.getDatabase().getItem("large milk"), 1), new ArtisanProduct("cheese", 100, 3, 345)),
            Map.of(Map.of(Game.getDatabase().getItem("goat milk"),1), new ArtisanProduct("goat cheese", 100, 3, 400)),
            Map.of(Map.of(Game.getDatabase().getItem("large goat milk"), 1), new ArtisanProduct("goat cheese", 100, 3, 600)))),
    Keg(CraftType.Keg, ),
//    Dehydrator(),
//    CharcoalKiln()
;

    private final CraftType craftType;
    private final List<Map<Map<Item, Integer>, ArtisanProduct>> items;

    ArtisanType(CraftType craftType, List<Map<Map<Item, Integer>, ArtisanProduct>> items) {
        this.craftType = craftType;

        this.items = items;

    }
}