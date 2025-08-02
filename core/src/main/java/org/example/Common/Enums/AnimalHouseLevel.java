package org.example.Common.Enums;

public enum AnimalHouseLevel {
    Small(4), Big(8), Deluxe(12);

    private final int capacity;
    AnimalHouseLevel(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }

    public boolean isMaxLevel () {
        return this == Deluxe;
    }

    public static AnimalHouseLevel fromString(String input) {
        if (input.contains("Big")) return Big;
        else if (input.contains("Deluxe")) return Deluxe;
        return Small;
    }
}
