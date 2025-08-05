package org.example.Common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Client.GameAssetManager;
import org.example.Common.Enums.ItemLevel;
import org.example.Common.Enums.Material;

import java.io.Serializable;

public class BasicItem implements Item , Serializable {
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
        return new TextureRegion(GameAssetManager.getInstance().getItemTexture(name));
    }

}
