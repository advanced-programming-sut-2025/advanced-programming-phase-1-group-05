package org.example.views;

import org.example.controllers.MenuController;
import org.example.models.Result;

public class GameMenu implements org.example.views.AppMenu {
    private final MenuController menuController;

    public GameMenu(MenuController menuController) {
        this.menuController = menuController;
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
        } else if (input.equalsIgnoreCase("user logout")){
            Result result = menuController.logoutUser();
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