package org.example.Common.Enums;

public enum AnimalType {
    CHICKEN, DUCK, RABBIT, DINOSAUR, // Coop animals
    COW, GOAT, SHEEP, PIG, // Barn animals
    CROW; //this mf

    public static  AnimalType fromString(String s) {
        for (AnimalType animalType : AnimalType.values()) {
            if (animalType.name().equalsIgnoreCase(s)) {
                return animalType;
            }
        }
        return null;
    }
}
