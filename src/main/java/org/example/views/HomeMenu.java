package org.example.views;

import org.example.controllers.GameManager;
import org.example.controllers.HomeMenuController;
import org.example.controllers.MenuController;
import org.example.models.Enums.HomeMenuCommands;
import org.example.models.Game;
import org.example.models.Result;

import java.util.regex.Matcher;

public class HomeMenu implements AppMenu {
    Matcher matcher = null;
    private final HomeMenuController controller = new HomeMenuController();
    private MenuController menuController;
    public HomeMenu(MenuController menuController) {
        this.menuController = menuController;
    }
    @Override
    public void handleUserInput(String input) {
        if(!Game.getGameMap().whereAmI().equals("Home"))
            System.out.println("You can only use that command when you're in your home!");
        else if((matcher = HomeMenuCommands.CraftingRecipes.getMatcher(input)) != null) {
            System.out.println(controller.showCraftingRecipes());
        } else if((matcher = HomeMenuCommands.CraftItem.getMatcher(input)) != null) {
            System.out.println(controller.craftItem(matcher.group("itemName")));
        } else if((matcher = HomeMenuCommands.CookingRefrigerator.getMatcher(input)) != null) {
            System.out.println(controller.putOrPickRefrigerator(matcher.group("action"), matcher.group("item")));
        } else if((matcher = HomeMenuCommands.ShowCookingRecipes.getMatcher(input)) != null) {
            System.out.println(controller.showLearntRecipes());
        } else if((matcher = HomeMenuCommands.CookingPrepare.getMatcher(input)) != null) {
            System.out.println(controller.prepareFood(matcher.group("recipeName")));
        } else if((matcher = HomeMenuCommands.ShowOptions.getMatcher(input)) != null) {
            System.out.println(controller.showOptions());
            int option = Game.getScanner().nextInt();
            if(option == 1) System.out.println(controller.showAllRecipes());
            else if(option == 2) System.out.println(controller.showAllCookingRecipes());
            else System.out.println("Not a valid option");
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        }
        else {
            System.out.println("Invalid Command!");
        }
    }

    @Override
    public String getMenuName() {
        return "Home Menu";
    }
}
