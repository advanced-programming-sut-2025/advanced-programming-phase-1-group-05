package org.example.Server.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.Item;
import org.example.Common.Enums.MineralType;

public class Mineral implements Item {
    private MineralType mineralType;
    private String name;
    private int price;

    public Mineral(MineralType mineralType) {
        this.mineralType = mineralType;
    }
    @Override
    public String getName() {
        return mineralType.getName();
    }

    @Override
    public int getPrice() {
        return mineralType.getPrice();
    }
    public TextureRegion getTexture() {
        return mineralType.getTexture();
    }
}
