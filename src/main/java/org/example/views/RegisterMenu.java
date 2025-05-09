package org.example.views;

import org.example.controllers.MenuController;
import org.example.controllers.RegisterMenuController;
import org.example.models.Enums.RegisterMenuCommand;
import org.example.models.Result;

import java.util.Scanner;

public class RegisterMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final RegisterMenuController registerController;
    private final Scanner scanner;

    public RegisterMenu(MenuController menuController, RegisterMenuController registerController,Scanner scanner) {
        this.menuController = menuController;
        this.registerController = registerController;
        this.scanner = scanner;
    }

    @Override
    public void handleUserInput(String input) {
        if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        }else if (input.equals("menu exit")) {
            Result result = menuController.exitMenu();
            System.out.println(result.getMessage());
            System.exit(0);
        } else if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.startsWith("register ")) {
            Result result = registerController.handleRegistration(input);
            System.out.println(result.getMessage());

            if (result.isSuccess()) {
                Result securityResult = registerController.showSecurityQuestions();
                System.out.println(securityResult.getMessage());

                if (securityResult.isSuccess()) {
                    menuController.enterMenu("login");
                }
            }
        } else if (input.equalsIgnoreCase("user logout")){
            Result result = menuController.logoutUser();
            System.out.println(result.getMessage());
        }else {
            System.out.println("Invalid Command!");
        }
    }
    @Override
    public String getMenuName() {
        return "Register Menu";
    }
}