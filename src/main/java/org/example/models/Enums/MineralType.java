package org.example.models.Enums;

public enum MineralType {
    Quartz("Quartz", 25),
    EarthCrystal(),
    FrozenTear(),
    FireQuarts();
    //etc...

    private final String name;
    private final int price;
    MineralType(String name, int price) {
        this.name = name;
        this.price = price;
    }
}