package org.example.models.Enums;

import org.example.models.Game;
import org.example.models.Item;
import org.example.models.Product;

import java.util.List;
import java.util.Map;

public enum ArtisanType {
    BeeHouse(CraftType.BeeHouse, 75, 96, Map.of(),List.of(new Product("Honey", 350, -1, null, null, null))),
    CheesePress(CraftType.CheesePress, 100, 3, Map.of(Game.getDatabase().getItem("Milk"), 1), List.of(new Product("cheese", 230, -1, null, null, null), new Product("goat cheese", 500, -1, null, null, null))),
//    Keg(),
//    Dehydrator(),
//    CharcoalKiln()
;

    private final CraftType craftType;
    private final Map<Item, Integer> items;
    private int energy;
    private int hours;

    ArtisanType(CraftType craftType, int energy, int time, Map<Item, Integer> items, List<Product> output) {
        this.craftType = craftType;
        this.energy = energy;
        this.items = items;
        this.hours = time;

    }
}