package org.example.models.Enums;

public enum FishingPoleType {
    Normal("Normal", 25, 8),
    Bamboo("Bamboo",500, 8),
    FiberGlass("FiberGlass",1800, 6),
    Iridium("Iridium",7500, 4);

    private final String name;
    private final int price;
    private final int energyUsage;
    FishingPoleType(String name, int price, int energyUsage) {
        this.name = name;
        this.price = price;
        this.energyUsage = energyUsage;
    }

    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public int getEnergyUsage() {
        return energyUsage;
    }
    public FishingPoleType nextLevel() {
        int nextOrdinal = this.ordinal() + 1;
        FishingPoleType[] levels = FishingPoleType.values();
        return nextOrdinal < levels.length ? levels[nextOrdinal] : this;
    }

    public boolean isMaxLevel() {
        return this == FishingPoleType.values()[FishingPoleType.values().length - 1];
    }
}
