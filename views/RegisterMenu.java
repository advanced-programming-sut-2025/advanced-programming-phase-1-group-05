package views;

import controllers.MenuController;
import controllers.RegisterMenuController;
import models.Result;

import java.util.Scanner;

public class RegisterMenu implements views.AppMenu {
    private final MenuController menuController;
    private final RegisterMenuController registerController;
    private final Scanner scanner;
    private static final int MAX_PASSWORD_ATTEMPTS = 3;

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
//            Result result = registerController.registerUser(input);
//            System.out.println(result.getMessage());
            handleRegistration(input);
        } else {
            System.out.println("Invalid Command!");
        }
    }
    private void handleRegistration(String initialInput) {
        int attempts = 0;
        String currentInput = initialInput;

        while (attempts < MAX_PASSWORD_ATTEMPTS) {
            Result result = registerController.registerUser(currentInput);
            System.out.println(result.getMessage());

            if (result.isSuccess()) {
                return;
            }

            if (result.getMessage().contains("Password and confirmation do not match")) {
                System.out.println("Please re-enter your password and confirmation:");
                System.out.println("Or type 'back' to return to registration menu");

                String newInput = scanner.nextLine().trim();
                if (newInput.equalsIgnoreCase("back")) {
                    return;
                }

                currentInput = newInput;
                attempts++;
            } else {
                return;
            }
        }

        System.out.println("Maximum password attempts reached. Returning to registration menu");
    }

    @Override
    public String getMenuName() {
        return "Register Menu";
    }
}