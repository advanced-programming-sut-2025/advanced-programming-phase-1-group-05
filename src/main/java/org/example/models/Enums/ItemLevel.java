package org.example.models.Enums;


public enum ItemLevel {
    Normal("Normal", 5, 40, 0, 1),
    Brass("Brass", 4, 55, 0.15, 1.25),
    Iron("Iron",3, 70,0.3, 1.25),
    Gold("Gold",2, 85,0.45, 1.5),
    Iridium("Iridium",1, 1000,0.6, 2);

    private final String name;
    private final int energyUsage;
    private final int wateringcanCapacity;
    private final double trashcanCoeff;
    private final double priceCoefficient;
    ItemLevel(String name, int energyUsage, int wateringcanCapacity, double trashcanCoeff, double priceCoefficient) {
        this.name = name;
        this.energyUsage = energyUsage;
        this.wateringcanCapacity = wateringcanCapacity;
        this.trashcanCoeff = trashcanCoeff;
        this.priceCoefficient = priceCoefficient;
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

    public double getPriceCoefficient() {
        return priceCoefficient;
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
