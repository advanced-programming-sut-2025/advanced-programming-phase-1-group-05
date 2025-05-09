package org.example.models.Enums;

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

    public AnimalHouseLevel upgrade() {
        return switch (this) {
            case Small -> Big;
            case Big, Deluxe -> Deluxe;
        };
    }
}