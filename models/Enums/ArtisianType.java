package models.Enums;

import models.Item;
import models.TimeAndDate;

import java.util.List;

public enum ArtisianType {
    BeeHouse("Bee House"),
    CheesePress(),
    Keg(),
    Dehydrator(),
    CharcoalKiln();

    private final String name;
    private final List<Item> items;
    private final int sellingPrice;

    ArtisianType(String name, List<Item> items, int sellingPrice) {
        this.name = name;
        this.items = items;
        this.sellingPrice = sellingPrice;

    }
}
