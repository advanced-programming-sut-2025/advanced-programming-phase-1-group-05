package org.example.controllers;

import org.example.models.*;
import org.example.models.Craft;
import org.example.models.Enums.CookingRecipeType;
import org.example.models.Enums.CraftType;
import org.example.models.Enums.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeMenuController {


    public Result showOptions(){
        return new Result(true, "please select an option :" + "\n" +"1. Crafting" + "\n" + "2. Cooking");
    }
  //  show all crafting recipes
    //is it even used?
    public Result showAllRecipes() {
        StringBuilder output = new StringBuilder();
        String error1 = "(you haven't learned this recipe yet)";
        String error2 = "(you don't have enough ingredients)";

        for (CraftType c : CraftType.values()) {
            output.append(c.getName()).append(" ");
            if (!Game.getCurrentPlayer().getBackPack().getLearntRecipes().contains(c))
                output.append(error1).append("\n");
            else {
                for (Material item : c.getIngredients().keySet()) {
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
        ArrayList<CraftType> recipes = Game.getCurrentPlayer().getBackPack().getLearntRecipes();
        StringBuilder output = new StringBuilder();
        output.append("** Your Crafting Recipes **\n");
        for(CraftType r : recipes) {
            output.append(r.getName()).append("\n");
        }
        return new Result(true, output.toString());
    }

    //craft item
    public Result craftItem(String itemName) {
        ArrayList<CraftType> craftingRecipes = Game.getCurrentPlayer().getBackPack().getLearntRecipes();
        Craft selectedRecipe = null;
        CraftType selectedCraftType = null;
        for (CraftType r : craftingRecipes) {
            if (r.getName().equals(itemName)) {
                selectedCraftType = r;
            }
        }
        selectedRecipe = new Craft(selectedCraftType);
        if (selectedCraftType == null) return new Result(false, "No recipe exits with that name");
        else if(Game.getCurrentPlayer().getBackPack().getLevel().getCapacity() ==
                Game.getCurrentPlayer().getBackPack().getInventory().size())
            return new Result(false, "Your inventory is full");
        boolean success = false;
        success = selectedRecipe.buildCraft();
        if(success) {
            Game.getCurrentPlayer().getBackPack().addToInventory(selectedRecipe, 1);
            return new Result(true, "Item crafted successfully and ready to be used");
        } else {
            return new Result(false, "You don't have enough ingredients");
        }
    }

    //put or pick from refrigerator
    public Result putOrPickRefrigerator(String action, String itemName) {
        HashMap<Food, Integer> refrigeratedFoods = Game.getCurrentPlayer().getFarm().getRefrigeratedFoods();
        switch (action) {
            case "pick": {
                if(Game.getCurrentPlayer().getBackPack().isInventoryFull())
                    return new Result(false, "Inventory is full");

                Food food = getFoodByName(itemName);
                if(food == null) return new Result(false, "No food with that name");

                Game.getCurrentPlayer().getBackPack().addToInventory(food, 1);
                Game.getCurrentPlayer().getFarm().removeRefrigeratedFood(food, 1);
                return new Result(true, "Selected food is now in your inventory");
            }
            case "put": {
                Food food = getFoodByName(itemName);
                if(food == null) return new Result(false, "No food with that name");

                Game.getCurrentPlayer().getBackPack().removeFromInventory(food, 1);
                Game.getCurrentPlayer().getFarm().addRefrigeratedFood(food, 1);
                return new Result(true, "Selected food is now in your refrigerator");
            }
        }
        return new Result(false, "Action is invalid");
    }

    //get food by name
    public Food getFoodByName(String foodName) {
        Item food = Game.getDatabase().getItem(foodName);
        if(food == null) return null;
        else if(food instanceof Food) {
            return (Food) food;
        } else {
            CookingRecipeType recipeType = CookingRecipeType.valueOf(foodName);
            return new Food(recipeType);
        }
    }

    //show learnt recipes
    public Result showLearntRecipes() {
        StringBuilder output = new StringBuilder();
        output.append("** Your Learnt Recipes **\n");
        for(Food recipe : Game.getCurrentPlayer().getCookingSkill().getLearntRecipes()) {
            output.append(recipe.getName()).append("\n");
        }
        return new Result(true, output.toString());
    }

    //prepare food
//    public Result prepareFood(String foodName) {
//        Food food = getFoodByName(foodName);
//        //errors
//        if(food == null) return new Result(false, "No food with that name");
//        if(!Game.getCurrentPlayer().getCookingSkill().getLearntRecipes().contains(food))
//            return new Result(false, "Aww you don't know how to cook this food");
//        for(Food f : food.getIngredients().keySet()) {
//            if(!Game.getCurrentPlayer().getBackPack().getInventory().containsKey(f))
//                return new Result(false, "You don't have all the ingredients in your inventory");
//        }
//        if(Game.getCurrentPlayer().getBackPack().isInventoryFull())
//            return new Result(false, "Inventory is full");
//
//        Game.getCurrentPlayer().getCookingSkill().cookFood(food);
//        return new Result(true, "Your food is cooked and ready!");
//    }

}
