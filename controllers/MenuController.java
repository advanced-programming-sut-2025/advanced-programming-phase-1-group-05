package controllers;

import models.Result;
import models.Enums.Menu;
import views.*;

import java.util.Scanner;

public class MenuController {
    private views.AppMenu currentMenu;
    private final Scanner scanner;

    public MenuController(Scanner scanner) {
        this.scanner = scanner;
        RegisterMenuController registerController = new RegisterMenuController(scanner);
        this.currentMenu = new RegisterMenu(this, registerController, this.getScanner());
    }

    public Result enterMenu(String menuName) {
        try {
            Menu targetMenu = Menu.valueOf(menuName.toUpperCase());

            if (!canSwitchToMenu(currentMenu, targetMenu)) {
                return new Result(false, "Invalid menu transition");
            }

            currentMenu = createMenuInstance(targetMenu);
            return new Result(true, "Entered " + menuName + " menu");
        } catch (IllegalArgumentException e) {
            return new Result(false, "Invalid menu name");
        }
    }

    private boolean canSwitchToMenu(AppMenu current, Menu target) {
        String currentMenuName = current.getMenuName();

        if (target == Menu.MAIN) return true;

        if (currentMenuName.equals("Main Menu")) return true;

        if ((currentMenuName.equals("Login Menu") && target == Menu.REGISTER) ||
                (currentMenuName.equals("Register Menu") && target == Menu.LOGIN)) {
            return true;
        }

        return false;
    }

    private AppMenu createMenuInstance(Menu menu) {
        switch (menu) {
            case LOGIN: return new LoginMenu(this);
            case MAIN: return new MainMenu(this);
            case PROFILE: return new ProfileMenu(this);
            case GAME: return new GameMenu(this);
            case REGISTER:
                RegisterMenuController registerController = new RegisterMenuController(this.getScanner());
                return new RegisterMenu(this, registerController, this.getScanner());
            default: throw new IllegalArgumentException("Unknown menu type");
        }
    }

    public Result exitMenu() {
        System.out.println("Exiting current menu...");
        return new Result(true, "Menu exited successfully");
    }

    public Result showCurrentMenu() {
        return new Result(true, "Current menu: " + currentMenu.getMenuName());
    }

    public void run() {
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equals("show current menu")) {
                Result result = showCurrentMenu();
                System.out.println(result.getMessage());
            }
            else {
                currentMenu.handleUserInput(input);
            }
        }
    }

    public Scanner getScanner() {
        return scanner;
    }
}