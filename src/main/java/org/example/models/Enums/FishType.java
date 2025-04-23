package org.example.models.Enums;

public enum FishType {

//    Salmon("Salmon", 75, Season.FALL, false),
//    Sardine(),
//    Shad(),
//    MidnightCarp(),
//    Squid(),
//    Tuna(),
//    Perch(),
//    Flounder(),
//    Lionfish(),
//    Herring(),
//    Ghostfish(),
//    Tilapia(),
//    Dorado(),
//    Sunfish(),
//    RainbowTrout(),
//    Legend(),
//    Glacierfish(),
//    Angler(),
//    Crimsonfish()
   ;


    private final String name;
    private final int price;
    private final Season season;
    private final boolean legendary;
    FishType(String name, int price, Season season, boolean legendary) {
        this.name = name;
        this.price = price;
        this.season = season;
        this.legendary = legendary;
    }
}