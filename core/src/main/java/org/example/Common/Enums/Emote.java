package org.example.Common.Enums;

public enum Emote {
    HAPPY(),
    SAD(),
    HEART(),
    EXCLAMATION(),
    NOTE(),
    SLEEP(),
    GAME(),
    QUESTION(),
    X(),
    PAUSE(),
    BLUSH(),
    ANGRY(),
    YES(),
    NO(),
    SICK(),
    LAUGH(),
    SURPRISED(),
    HI(),
    TAUNT(),
    UH(),
    MUSIC(),
    JAR()
    ;


    public String getName() {
       return toString().toLowerCase();
    }

    public String getTexturePath() {
         return "emotes/" + getName() + ".png";
    }

    @Override
    public String toString() {
        String name = name().toLowerCase();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
