package org.example.models.Enums;

import org.example.models.Result;

public enum Weather {
    Sunny(1, 1.5),
    Rain(1.5, 1.2),
    Storm(1.5, 4),
    Snow(1.5, 0);

    private final double energyCoefficient;
    private final double fishingCoefficient;
    Weather(double energyCoefficient, double fishingCoefficient) {
        this.energyCoefficient = energyCoefficient;
        this.fishingCoefficient = fishingCoefficient;
    }

    public Result thunder(int x, int y){
        //change tile type to soil
        return null;
    }

//    @Override
//    public String toString() {
//        return super.toString();
//    }
    @Override
    public String toString() {
        return switch (this) {
            case Sunny -> "Sunny ☀️";
            case Rain -> "Rain 🌧️";
            case Storm -> "Storm ⛈️";
            case Snow -> "Snow ❄️";
        };
    }

}