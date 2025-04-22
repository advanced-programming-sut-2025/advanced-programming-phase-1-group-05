package org.example.models.Enums;

public enum Season {
    SPRING,
    SUMMER,
    FALL,
    WINTER;

    public Season next(){
        return switch (this){
            case SPRING ->  SUMMER;
            case SUMMER ->  FALL;
            case FALL ->  WINTER;
            default ->  SPRING;
        };
    }
}