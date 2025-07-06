package org.example.models.Enums;

public enum TileType {
    Water("Water.png", false),
    Soil("Soil.png", false),
    Flat("Flat.png", false),
    House("Building.png", true),
    GreenHouse("GreenHouse.png", true),
    Tree("tree.png", true),
    CheatThor("default.png", false),
    Stone("Stone.png", false),
    Player("frame_0_2.png", false),
    HOUSE_1("house/1.png", false),
    HOUSE_2("house/2.png", false),
    HOUSE_3("house/3.png", false),
    HOUSE_4("house/4.png", false),
    HOUSE_5("house/5.png", false),
    HOUSE_6("house/6.png", false),
    HOUSE_7("house/7.png", false),
    HOUSE_8("house/8.png", false),
    HOUSE_9("house/9.png", false),
    HOUSE_10("house/10.png", false),
    HOUSE_11("house/11.png", false),
    HOUSE_12("house/12.png", false),
    HOUSE_13("house/13.png", false),
    HOUSE_14("house/14.png", false),
    HOUSE_15("house/15.png", false),
    HOUSE_16("house/16.png", false),
    GREENHOUSE_1("greenhouse/1.png", true),
    GREENHOUSE_2("greenhouse/2.png", true),
    GREENHOUSE_3("greenhouse/3.png", true),
    GREENHOUSE_4("greenhouse/4.png", true),
    GREENHOUSE_5("greenhouse/5.png", true),
    GREENHOUSE_6("greenhouse/6.png", true),
    GREENHOUSE_7("greenhouse/7.png", true),
    GREENHOUSE_8("greenhouse/8.png", true),
    GREENHOUSE_9("greenhouse/9.png", true),
    GREENHOUSE_10("greenhouse/10.png", true),
    GREENHOUSE_11("greenhouse/11.png", true),
    GREENHOUSE_12("greenhouse/12.png", true),
    GREENHOUSE_13("greenhouse/13.png", true),
    GREENHOUSE_14("greenhouse/14.png", true),
    GREENHOUSE_15("greenhouse/15.png", true),
    GREENHOUSE_16("greenhouse/16.png", true),
    Wood("Wood.png",false),
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
