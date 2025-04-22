package org.example.models.Enums;

public enum CoopAndBarnType {
    Small(4), Big(8), Deluxe(12);

    private int capacity;
    CoopAndBarnType(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
}