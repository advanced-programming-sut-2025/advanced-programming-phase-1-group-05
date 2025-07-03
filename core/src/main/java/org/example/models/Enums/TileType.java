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
