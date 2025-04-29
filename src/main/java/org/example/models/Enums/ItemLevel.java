package org.example.models.Enums;


public enum ItemLevel {
    Normal("Normal", 5, 40, 0),
    Brass("Brass", 4, 55, 0.15),
    Iron("Iron",3, 70,0.3 ),
    Gold("Gold",2, 85,0.45),
    Iridium("Iridium",1, 1000,0.6);

    private final String name;
    private final int energyUsage;
    private final int wateringcanCapacity;
    private final double trashcanCoeff;
    ItemLevel(String name, int energyUsage, int wateringcanCapacity, double trashcanCoeff) {
        this.name = name;
        this.energyUsage = energyUsage;
        this.wateringcanCapacity = wateringcanCapacity;
        this.trashcanCoeff = trashcanCoeff;
    }
    public String getName() {
        return name;
    }
    public int getEnergyUsage() {
        return energyUsage;
    }
    public int getWateringcanCapacity() {
        return wateringcanCapacity;
    }
    public double getTrashcanCoeff() {
        return trashcanCoeff;
    }
    public ItemLevel upgradeLevel() {
        return switch (this) {
            case Normal -> Brass;
            case Brass -> Iron;
            case Iron -> Gold;
            case Gold, Iridium -> Iridium;
        };
    }

    public boolean isMaxLevel() {
        return this == ItemLevel.values()[ItemLevel.values().length - 1];
    }
}
