package org.example.models.Enums;

public enum BackPackType {
    Normal("Normal", 12),
    Big("Big", 24),
    Deluxe("Deluxe", 1000000);

    private final String name;
    private final int capacity;
    private BackPackType(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }
    public int getCapacity() {
        return capacity;
    }
    public BackPackType nextLevel() {
        int nextOrdinal = this.ordinal() + 1;
        BackPackType[] levels = BackPackType.values();
        return nextOrdinal < levels.length ? levels[nextOrdinal] : this;
    }

    public boolean isMaxLevel() {
        return this == BackPackType.values()[BackPackType.values().length - 1];
    }
}
