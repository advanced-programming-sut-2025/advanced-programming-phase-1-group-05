package org.example.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.controllers.GameManager;

import java.util.List;
import java.util.Map;

public class ArtisanProduct extends Product implements Item {
    private final String name;
    private final int energy;
    private final int processingHours;
    private final int sellPrice;
    private int startHour;
    public ArtisanProduct(String name, int energy, int processingTime, int sellPrice) {
        super(name, sellPrice, -1, null, List.of(), Map.of());
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

    public TextureRegion getTexture() {
        return null;//TODO implement
    }
}
