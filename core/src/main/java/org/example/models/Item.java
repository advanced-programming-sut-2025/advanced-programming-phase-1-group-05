package org.example.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Enums.Material;

import java.util.Map;

public interface Item extends Material {
    String getName();
    int getPrice();
    TextureRegion getTexture();
}
