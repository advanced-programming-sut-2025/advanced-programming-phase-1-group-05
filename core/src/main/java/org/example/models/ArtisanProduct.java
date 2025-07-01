package org.example.models;

import org.example.controllers.GameManager;

public class ArtisanProduct implements Item {
    private final String name;
    private final int energy;
    private final int processingHours;
    private final int sellPrice;
    private int startHour;
    public ArtisanProduct(String name, int energy, int processingTime, int sellPrice) {
        this.name = name;
        this.energy = energy;
        processingHours = processingTime;
        this.sellPrice = sellPrice;
        MyGame.getDatabase().itemDatabase.add(this);
        startHour = GameManager.getGameClock().getTotalHours();
    }

    public boolean isReady () {
        return GameManager.getGameClock().getTotalHours() >= startHour + processingHours;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return sellPrice;
    }

    public int getEnergy() {
        return energy;
    }

    public int getProcessingTime() {
        return processingHours;
    }


}
