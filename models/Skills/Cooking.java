package models.Skills;

import models.Player;

public class Cooking implements Skill{
    private Inventory fridge;
    private Map<String, Map<String, Integer>> knownRecipes;
    int level = 0;
    int capacity = 0;


    public Cooking() {
        this.fridge = new Inventory();
        this.knownRecipes = new HashMap<>();
    }

    public void transferToFridge(String itemName, Inventory playerInventory) {}

    public void takeFromFridge(String itemName, Inventory playerInventory) {}

    public void learnRecipe(String foodName, Map<String, Integer> requiredIngredients) {}

    public void cookFood(String recipeName) {}

    public void consumeFood(String food, Player player) {}
    @Override
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public void increaseLevel() {
        if(level < 4) this.level ++;
    }
    @Override
    public boolean canGoToNextLevel() {
        if((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1)*100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
}
