package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public enum BackPackType {
    Normal("Training", 12, "Stardew_Valley_Images-main/extra/trainingInventory.png", "Stardew_Valley_Images-main/Tools/Backpack.png", "Stardew_Valley_Images-main/craft_view/craft 12.png"),
    Big("Big", 24, "Stardew_Valley_Images-main/extra/bigInventory.png", "Stardew_Valley_Images-main/Tools/36_Backpack.png", "Stardew_Valley_Images-main/craft_view/craft 24.png"),
    Deluxe("Deluxe", 1000000, "Stardew_Valley_Images-main/extra/deluxInventory.png", "Stardew_Valley_Images-main/Tools/36_Backpack.png", "Stardew_Valley_Images-main/craft_view/craft 24.png");

    private final String name;
    private final int capacity;
    private final String inventoryTexturePath;
    private final String backpackTexturePath;
    private final String craftTexturePath;
    private final TextureRegion inventoryTexture;
    private final TextureRegion backPackTexture;
    private final TextureRegion craftTexture;
    BackPackType(String name, int capacity, String inventoryTexturePath, String backpackTexturePath, String craftTexturePath) {
        this.name = name;
        this.capacity = capacity;
        this.inventoryTexturePath = inventoryTexturePath;
        this.backpackTexturePath = backpackTexturePath;
        this.inventoryTexture = new TextureRegion(new Texture(inventoryTexturePath));
        this.backPackTexture = new TextureRegion(new Texture(backpackTexturePath));
        this.craftTexturePath = craftTexturePath;
        this.craftTexture = new TextureRegion(new Texture(craftTexturePath));

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
        return this.inventoryTexture;
    }
    public TextureRegion getBackPackTexture() {
        return this.backPackTexture;
    }
    public TextureRegion getCraftTexture() {
        return this.craftTexture;
    }
}
