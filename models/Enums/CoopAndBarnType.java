package models.Enums;

public enum CoopAndBarnType {
    Small(4), Big(8), Delux(12);

    private int capacity;
    CoopAndBarnType(int capacity) {
        this.capacity = capacity;
    }
    public int getCapacity() {
        return capacity;
    }
}
