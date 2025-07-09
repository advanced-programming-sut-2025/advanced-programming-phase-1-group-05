package org.example.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Enums.Material;

public class ForagingItem implements Item{
    private final String name;
    private final int price;
    private final Material material;

    public ForagingItem(Material material, String name, int price) {
        this.material = material;
        this.name = name;
        this.price = price;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return price;
    }
    public TextureRegion getTexture() {
        return null; //TODO implement
    }
}
