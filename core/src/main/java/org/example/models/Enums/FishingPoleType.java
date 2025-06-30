package org.example.models.Enums;

public enum FishingPoleType {
    Training("Training", 25, 8, 0.1),
    Bamboo("Bamboo",500, 8, 0.5),
    FiberGlass("FiberGlass",1800, 6, 0.9),
    Iridium("Iridium",7500, 4, 1.2);

    private final String name;
    private final int price;
    private final int energyUsage;
    private final double fishingCoefficient;
    FishingPoleType(String name, int price, int energyUsage, double fishingCoefficient) {
        this.name = name;
        this.price = price;
        this.energyUsage = energyUsage;
        this.fishingCoefficient = fishingCoefficient;
    }

    public static FishingPoleType fromString (String input) {
        input = input.toLowerCase();
        switch (input) {
            case "training rod": {
                return Training;
            }
            case "bamboo pole": {
                return Bamboo;
            }
            case "fiberglass rod": {
                return FiberGlass;
            }
            case "iridium rod" : {
                return Iridium;
            }
        }
        return null;
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

    public double getFishingCoefficient() {
        return fishingCoefficient;
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
