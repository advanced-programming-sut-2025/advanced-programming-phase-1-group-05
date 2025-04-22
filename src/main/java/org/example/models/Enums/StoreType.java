package org.example.models.Enums;

import org.example.models.Product;

import java.util.List;


public enum StoreType {
    BlackSmith("Black Smith", 9, 16, List.of(new Product("Copper Ore", "A common ore that can be smelted into bars.", 75, -1))),
    JojaMart("Joja Mart", 9, 23, List.of(new Product("Joja Cola", "The flagship product of Joja corporation.", 75, -1))),
    PierreGeneralStore("Pierre's General Store", 9, 17, List.of(new Product("Beer", "Drink in moderation.", 400, -1))),
    CarpenterShop("Carpenter Shop",9,20, List.of(new Product("Wood", "A sturdy, yet flexible plant material with a wide variety of uses.", 10, -1))),
    FishShop("Fish Shop",9,17, List.of(new Product("Joja Cola", "The flagship product of Joja corporation.", 75, -1))),
    MarnieRanch("Marnie Ranch",9,16),
    StardropSaloon("Stardrop Saloon",12,24);
    //different products for each store
    private final String storeName;
    private final int workHourStart;
    private final int workHourEnd;
    private final List<Product> products;
    StoreType(String storeName, int workHourStart, int workHourEnd, List<Product> products) {
        this.storeName = storeName;
        this.workHourStart = workHourStart;
        this.workHourEnd = workHourEnd;
        this.products = products;
    }

    public String getStoreName() {
        return storeName;
    }
    public int getWorkHourStart() {
        return workHourStart;
    }
    public int getWorkHourEnd() {
        return workHourEnd;
    }
    public List<Product> getProducts() {
        return products;
    }
}