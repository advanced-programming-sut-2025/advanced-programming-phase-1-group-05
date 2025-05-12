package org.example.models.Enums;

import java.util.List;

public enum Season {
    SPRING("Spring"),
    SUMMER("Summer"),
    FALL("Fall"),
    WINTER("Winter");

    private final String name;

    Season(String name) {
        this.name = name;
    }
    public Season next() {
        return switch (this){
            case SPRING ->  SUMMER;
            case SUMMER ->  FALL;
            case FALL ->  WINTER;
            default ->  SPRING;
        };
    }
    @Override
    public String toString() {
        return name;
    }
}