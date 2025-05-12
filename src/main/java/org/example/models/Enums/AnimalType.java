package org.example.models.Enums;

public enum AnimalType {
    CHICKEN, DUCK, RABBIT, // Coop animals
    COW, GOAT, SHEEP, PIG;   // Barn animals

    public static  AnimalType fromString(String s) {
        for (AnimalType animalType : AnimalType.values()) {
            if (animalType.name().equalsIgnoreCase(s)) {
                return animalType;
            }
        }
        return null;
    }
}
