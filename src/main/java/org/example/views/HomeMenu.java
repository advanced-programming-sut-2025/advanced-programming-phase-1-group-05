package org.example.views;

import org.example.controllers.HomeMenuController;
import org.example.models.Enums.HomeMenuCommands;
import org.example.models.Game;

import java.util.regex.Matcher;

public class HomeMenu implements AppMenu {
    Matcher matcher = null;
    private final HomeMenuController controller = new HomeMenuController();
    private boolean showedOptions = false;
    @Override
    public void handleUserInput(String input) {
        if (!showedOptions) {
            controller.showOptions();
            showedOptions = true;
            //no logic in view
            int selectedOption = Game.getScanner().nextInt();
            if(selectedOption == 1) controller.showAllRecipes();
            else if(selectedOption == 2) controller.showAllCookingRecipes();
        }
        if(!Game.getGameMap().whereAmI().equals("Home"))
            System.out.println("You can only use that command when you're in your home!");
        else if((matcher = HomeMenuCommands.CraftingRecipes.getMatcher(input)) != null) {
            System.out.println(controller.showCraftingRecipes());
        } else if((matcher = HomeMenuCommands.CraftItem.getMatcher(input)) != null) {
            System.out.println(controller.craftItem(matcher.group("item_name")));
        } else if((matcher = HomeMenuCommands.CookingRefrigerator.getMatcher(input)) != null) {
            System.out.println(controller.putOrPickRefrigerator(matcher.group("action"), matcher.group("item")));
        } else if((matcher = HomeMenuCommands.ShowCookingRecipes.getMatcher(input)) != null) {
            System.out.println(controller.showLearntRecipes());
        } else if((matcher = HomeMenuCommands.CookingPrepare.getMatcher(input)) != null) {
            System.out.println(controller.prepareFood(matcher.group("recipe_name")));
        } else {
            System.out.println("Invalid Command!");
        }
    }

    @Override
    public String getMenuName() {
        return "Home Menu";
    }
}
