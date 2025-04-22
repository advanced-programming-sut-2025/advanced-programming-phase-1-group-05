package views;


import controllers.LoginMenuController;
import controllers.MenuController;
import models.Result;

import java.util.Scanner;


public class LoginMenu implements views.AppMenu {
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

        if (waitingForNewPassword) {
            handleNewPassword(input);
            return;
        }

        if (inPasswordRecovery) {
            handleSecurityAnswer(input);
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
            handleLoginCommand(input);
        } else if (input.equals("menu exit")) {
            Result result = menuController.exitMenu();
            System.out.println(result.getMessage());
            System.exit(0);
        } else if (input.startsWith("forget password -u")) {
            startPasswordRecovery(input);
        } else {
            System.out.println("Invalid Command!");
        }
    }
    private void handleLoginCommand(String input) {
        String[] parts = input.split(" -");
        String username = null;
        String password = null;
        boolean stayLoggedIn = false;

        for (String part : parts) {
            if (part.startsWith("u ")) {
                username = part.substring(2).trim();
            } else if (part.startsWith("p ")) {
                password = part.substring(2).trim();
            } else if (part.equals("stay-logged-in")) {
                stayLoggedIn = true;
            }
        }

        if (username == null || password == null) {
            System.out.println("Invalid login command format!");
            return;
        }
        Result result = (loginController.loginUser(username, password, stayLoggedIn));
        System.out.println(result.getMessage());
    }

    private void startPasswordRecovery(String input) {
        try {
            String username = input.substring("forget password -u ".length()).trim();
            Result result = loginController.initiatePasswordRecovery(username);
            System.out.println(result.getMessage());

            if (result.isSuccess()) {
                inPasswordRecovery = true;
            }
        } catch (StringIndexOutOfBoundsException e) {
            System.out.println("Invalid command format! Use: forget password -u <username>");
        }
    }

    private void handleSecurityAnswer(String input) {
        if (input.startsWith("answer -a ")) {
            String answer = input.substring("answer -a ".length()).trim();
            Result result = (loginController.verifySecurityAnswer(answer));
            System.out.println(result.getMessage());

            if (result.isSuccess()) {
                inPasswordRecovery = false;
                waitingForNewPassword = true;
            } else {
                inPasswordRecovery = false;
            }
        } else {
            System.out.println("Please use the format: answer -a <your_answer>");
        }
    }
    private void handleNewPassword(String input) {
        Result result = (loginController .setNewPassword(input));
        System.out.println(result.getMessage());
        waitingForNewPassword = false;
    }

    @Override
    public String getMenuName() {
        return "Login Menu";
    }
}
