package org.example.models.Enums;

public enum BuildingType {
    GreenHouse(1000),
    Coop(0),
    Barn(0),
    Store(0);


    private final int price;
    BuildingType(int price) {
        this.price = price;
    }

    public BuildingType getType(String type) {
        if (type.equalsIgnoreCase("GreenHouse"))
            return BuildingType.GreenHouse;
        else if (type.equalsIgnoreCase("Coop"))
            return BuildingType.Coop;
        else if (type.equalsIgnoreCase("Barn"))
            return BuildingType.Barn;
        return null;
    }

    public static BuildingType fromString(String name) {
        if (name == null) return null;

        for (BuildingType type : BuildingType.values()) {
            if (type.toString().equalsIgnoreCase(name)) return type;
        }
        return null;
    }
}