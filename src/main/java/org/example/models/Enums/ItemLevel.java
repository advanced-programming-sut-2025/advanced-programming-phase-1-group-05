package org.example.models.Enums;


public enum ItemLevel {
    Normal("Normal", 5),
    Brass("Brass", 4),
    Iron("Iron",3),
    Gold("Gold",2),
    Iridium("Iridium",1);

    private final String name;
    private final int energyUsage;
    ItemLevel(String name, int energyUsage) {
        this.name = name;
        this.energyUsage = energyUsage;
    }
    public String getName() {
        return name;
    }
    public int getEnergyUsage() {
        return energyUsage;
    }
    public ItemLevel nextLevel() {
        int nextOrdinal = this.ordinal() + 1;
        ItemLevel[] levels = ItemLevel.values();
        return nextOrdinal < levels.length ? levels[nextOrdinal] : this;
    }

    public boolean isMaxLevel() {
        return this == ItemLevel.values()[ItemLevel.values().length - 1];
    }
}
