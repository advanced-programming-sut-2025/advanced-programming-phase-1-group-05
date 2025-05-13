package org.example.models;

import org.example.models.Enums.CookingRecipeType;
import org.example.models.Enums.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Food implements Item{
    private CookingRecipeType recipeType;

    public Food(CookingRecipeType cookingRecipeType) {
        this.recipeType = cookingRecipeType;
    }

    public CookingRecipeType getRecipeType() {
        return recipeType;
    }
    public Map<Material, Integer> getIngredients() {
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
}
