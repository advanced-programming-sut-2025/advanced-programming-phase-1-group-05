package org.example.views;

import org.example.controllers.GameMenuController;
import org.example.controllers.MenuController;
import org.example.models.Result;


public class GameMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final GameMenuController gameMenuController;

    public GameMenu(MenuController menuController, GameMenuController gameMenuController) {
        this.menuController = menuController;
        this.gameMenuController = gameMenuController;
    }

    @Override
    public void handleUserInput(String input) {
        if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.startsWith("game new")) {
            Result result = gameMenuController.newGame(input);
            System.out.println(result.getMessage());
        } else if (input.startsWith("game map")) {
            Result result = gameMenuController.chooseMap(input);
            System.out.println(result.getMessage());
        } else if (input.equalsIgnoreCase("load game")) {
            Result result = gameMenuController.loadGame();
            System.out.println(result.getMessage());
        } else if (input.equalsIgnoreCase("exit game")) {
            Result result = gameMenuController.exitGame();
            System.out.println(result.getMessage());
        } else if (input.equalsIgnoreCase("next turn")) {
            Result result = gameMenuController.nextTurn();
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid Command!");
        }
    }
    @Override
    public String getMenuName() {
        return "Game Menu";
    }
}