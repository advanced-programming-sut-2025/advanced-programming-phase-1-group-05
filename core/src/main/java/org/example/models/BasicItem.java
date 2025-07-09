package org.example.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Enums.ItemLevel;
import org.example.models.Enums.Material;

import java.util.Map;

public class BasicItem implements Item {
    private String name;
    private int price;
    private ItemLevel level;
    private Material material;
    public BasicItem(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
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
        return null;//TODO implement
    }

}
