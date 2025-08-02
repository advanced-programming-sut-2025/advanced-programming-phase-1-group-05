package org.example.Common.Enums;

public enum AnimalType {
    CHICKEN, DUCK, RABBIT, // Coop animals
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

    @Override
    public String toString() {
        switch (this) {
            case CHICKEN: return "🐔";
            case DUCK: return "🦆";
            case RABBIT: return "🐰";
            case COW: return "🐮";
            case GOAT: return "🐐";
            case SHEEP: return "🐑";
            case PIG: return "🐷";
        }
        return "😃";
    }
}
