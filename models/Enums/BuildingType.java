package models.Enums;

public enum BuildingType {
    GreenHouse(1000);


    private final int price;
    BuildingType(int price) {
        this.price = price;
    }
}
