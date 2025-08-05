package org.example.Server.models;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.Item;
import org.example.Common.Enums.CookingRecipeType;

import java.io.Serializable;
import java.util.Map;

public class Food implements Item , Serializable {
    private CookingRecipeType recipeType;


    public Food(CookingRecipeType cookingRecipeType) {
        this.recipeType = cookingRecipeType;
    }

    public CookingRecipeType getRecipeType() {
        return recipeType;
    }
    public Map<Item, Integer> getIngredients() {
        return recipeType.getIngredients();
    }
    public int getEnergy() {
        return recipeType.Energy();
    }
    @Override
    public String getName() {
        return recipeType.getName();
    }
    @Override
    public int getPrice() {
        return 0;
    }
    public TextureRegion getTexture() {
        return recipeType.getTexture();
    }
}
