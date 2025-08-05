package org.example.Common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.Enums.Material;

public interface Item extends Material{
    String getName();
    int getPrice();
    TextureRegion getTexture();
}
