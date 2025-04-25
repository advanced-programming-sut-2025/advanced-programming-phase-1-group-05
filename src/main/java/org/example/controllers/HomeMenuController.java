package org.example.controllers;

import org.example.models.Craft;
import org.example.models.Game;
import org.example.models.Item;
import org.example.models.Craft;
import org.example.models.Result;

import java.util.ArrayList;
import java.util.List;

public class HomeMenuController {

    //show all crafting recipes
    public Result showAllRecipes() {
        ArrayList<Craft> recipes = Game.getDatabase().getCraftingRecipeDatabase();
        StringBuilder output = new StringBuilder();
        String error1 = "(you haven't learned this recipe yet)";
        String error2 = "(you don't have enough ingredients)";

        for (Craft r : recipes) {
            output.append(r.getName()).append(" ");
            if (!Game.getCurrentPlayer().getBackPack().getLearntRecipes().contains(r))
                output.append(error1).append("\n");
            else {
                for (Item item : r.getIngredients()) {
                    if (!Game.getCurrentPlayer().getBackPack().getInventory().containsKey(item)) {
                        output.append(error2).append("\n");
                        break;
                    }
                }
            }
        }
        return new Result(true, output.toString());
    }

    //show crafting recipes
    public Result showCraftingRecipes() {
        ArrayList<Craft> recipes = Game.getCurrentPlayer().getBackPack().getLearntRecipes();
        StringBuilder output = new StringBuilder();
        output.append("** Your Crafting Recipes **\n");
        for(Craft r : recipes) {
            output.append(r.getName()).append("\n");
        }
        return new Result(true, output.toString());
    }

    //craft item
    public Result craftItem(String itemName) {
        ArrayList<Craft> craftingRecipes = Game.getCurrentPlayer().getBackPack().getLearntRecipes();
        Craft selectedRecipe = null;
        for (Craft r : craftingRecipes) {
            if (r.getName().equals(itemName)) {
                selectedRecipe = r;
            }
        }
        if (selectedRecipe == null) return new Result(false, "No recipe exits with that name");
        else if(Game.getCurrentPlayer().getBackPack().getLevel().getCapacity() ==
                Game.getCurrentPlayer().getBackPack().getInventory().size())
            return new Result(false, "Your inventory is full");

        Game.getCurrentPlayer().getBackPack().addToInventory(selectedRecipe, 1);
        return new Result(true, "Item crafted successfully and ready to be used");
    }


}
