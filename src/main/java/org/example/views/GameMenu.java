package org.example.views;

import org.example.controllers.GameMenuController;
import org.example.controllers.MenuController;
import org.example.controllers.RegisterMenuController;
import org.example.models.Result;

public class GameMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final GameMenuController gameController;

    public GameMenu(MenuController menuController, GameMenuController gameController) {
        this.menuController = menuController;
        this.gameController = gameController;
    }

    @Override
    public void handleUserInput(String input) {
        System.out.println("\n=== Game Menu ===");
        if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        }
        else if (input.matches("meet\\s+NPC\\s+\\S+")){
            int CIndex = input.indexOf('C');
            input = input.substring(CIndex + 1).trim();
            System.out.println(gameController.meetNPC(input).getMessage());
        }
        else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
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