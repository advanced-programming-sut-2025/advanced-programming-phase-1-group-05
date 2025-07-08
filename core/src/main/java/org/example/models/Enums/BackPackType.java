package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum BackPackType {
    Normal("Training", 12, "Stardew_Valley_Images-main/extra/trainingInventory.png"),
    Big("Big", 24, "Stardew_Valley_Images-main/extra/bigInventory.png"),
    Deluxe("Deluxe", 1000000, "Stardew_Valley_Images-main/extra/deluxInventory.png");

    private final String name;
    private final int capacity;
    private final String inventoryTexturePath;
    BackPackType(String name, int capacity, String inventoryTexturePath) {
        this.name = name;
        this.capacity = capacity;
        this.inventoryTexturePath = inventoryTexturePath;
    }

    public String getName() {
        return name;
    }
    public int getCapacity() {
        return capacity;
    }
    public BackPackType nextLevel() {
        int nextOrdinal = this.ordinal() + 1;
        BackPackType[] levels = BackPackType.values();
        return nextOrdinal < levels.length ? levels[nextOrdinal] : this;
    }

    public boolean isMaxLevel() {
        return this == BackPackType.values()[BackPackType.values().length - 1];
    }
    public TextureRegion getInventoryTexture() {
        return new TextureRegion(new Texture(this.inventoryTexturePath));
    }
}
