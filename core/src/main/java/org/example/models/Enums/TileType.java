package org.example.models.Enums;
//
//public enum TileType {
//    Water,
//    Soil,
//    Flat,
//    Stone,
//    Tree,
//    Mine,
//    CheatThor,
//    Building,
//    GreenHouse,
//    Player,
//    Occupied;
//}

public enum TileType {
    Water("Water.png", false),
    Soil("Soil.png", false),
    Flat("Flat.png", false),
    House("Building.png", true),
    GreenHouse("GreenHouse.png", true),
    Tree("tree.png", true),
    CheatThor("default.png", false),
    Stone("default.png", false),
    Player("frame_0_2.png", false),
    HOUSE_1("house/1.png", true),
    HOUSE_2("house/2.png", true),
    HOUSE_3("house/3.png", true),
    HOUSE_4("house/4.png", true),
    HOUSE_5("house/5.png", true),
    HOUSE_6("house/6.png", true),
    HOUSE_7("house/7.png", true),
    HOUSE_8("house/8.png", true),
    HOUSE_9("house/9.png", true),
    HOUSE_10("house/10.png", true),
    HOUSE_11("house/11.png", true),
    HOUSE_12("house/12.png", true),
    HOUSE_13("house/13.png", true),
    HOUSE_14("house/14.png", true),
    HOUSE_15("house/15.png", true),
    HOUSE_16("house/16.png", true),
    Mine("default.png", false);

    private final String imagePath;
    private final boolean largeStructure;

    TileType(String imagePath, boolean largeStructure) {
        this.imagePath = imagePath;
        this.largeStructure = largeStructure;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isLargeStructure() {
        return largeStructure;
    }
}
