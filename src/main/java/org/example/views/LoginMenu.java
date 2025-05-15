package org.example.views;


import org.example.controllers.MenuController;
import org.example.controllers.LoginMenuController;
import org.example.models.Result;

import java.util.Scanner;

public class LoginMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private boolean inPasswordRecovery = false;
    private boolean waitingForNewPassword = false;
    private final LoginMenuController loginController;
    private final Scanner scanner;

    public LoginMenu(MenuController menuController, LoginMenuController loginController, Scanner scanner) {
        this.menuController = menuController;
        this.loginController = loginController;
        this.scanner = scanner;
    }

    @Override
    public void handleUserInput(String input) {

        if (loginController.isWaitingForNewPassword()) {
            Result result = loginController.handleNewPassword(input);
            System.out.println(result.getMessage());
            return;
        }

        if (loginController.isInPasswordRecovery()) {
            Result result = loginController.handleSecurityAnswer(input);
            System.out.println(result.getMessage());
            return;
        }
        if (input.startsWith("menu enter ")) {
            String menuName = input.substring("menu enter ".length()).trim();
            Result result = menuController.enterMenu(menuName);
            System.out.println(result.getMessage());
        } else if (input.equals("show current menu")) {
            Result result = menuController.showCurrentMenu();
            System.out.println(result.getMessage());
        } else if (input.startsWith("login -u ")) {
            Result result = loginController.handleLoginCommand(input);
            System.out.println(result.getMessage());
            if (result.isSuccess()) {
                menuController.enterMenu("main");
            }
        } else if (input.equals("menu exit")) {
            Result result = menuController.exitMenu();
            System.out.println(result.getMessage());
            System.exit(0);
        } else if (input.startsWith("forget password -u")) {
            Result result = loginController.startPasswordRecovery(input);
            System.out.println(result.getMessage());
        }else if (input.equalsIgnoreCase("user logout")){
            Result result = menuController.logoutUser();
            System.out.println(result.getMessage());
        } else if (input.equals("show menus")) {
            menuController.showMenus();
        } else {
            System.out.println("Invalid Command!");
        }
    }


    @Override
    public String getMenuName() {
        return "Login Menu";
    }
}