package org.example.models;

public class ArtisanProduct implements Item {
    private final String name;
    private final int energy;
    private final int processingHours;
    private final int sellPrice;

    public ArtisanProduct(String name, int energy, int processingTime, int sellPrice) {
        this.name = name;
        this.energy = energy;
        processingHours = processingTime;
        this.sellPrice = sellPrice;
        Game.getDatabase().itemDatabase.add(this);
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
