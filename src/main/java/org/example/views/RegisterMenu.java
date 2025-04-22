package org.example.views;

import org.example.controllers.MenuController;
import org.example.controllers.RegisterMenuController;
import org.example.models.Result;

public class RegisterMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final RegisterMenuController registerController;

    public RegisterMenu(MenuController menuController, RegisterMenuController registerController) {
        this.menuController = menuController;
        this.registerController = registerController;
    }

    @Override
    public void handleUserInput(String input) {
        System.out.println("\n=== Register Menu ===");
        if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        }else if (input.equals("menu exit")) {
            Result result = menuController.exitMenu();
            System.out.println(result.getMessage());
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.startsWith("register ")) {
            Result result = registerController.registerUser(input);
            System.out.println(result.getMessage());
        } else {
            System.out.println("Invalid Command!");
        }
    }
    @Override
    public String getMenuName() {
        return "Register Menu";
    }
}