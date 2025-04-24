package org.example.controllers;

import org.example.models.Enums.Menu;
import org.example.models.Result;
import org.example.models.User;
import org.example.views.*;

import java.util.Scanner;

public class MenuController {
    private org.example.views.AppMenu currentMenu;
    private final Scanner scanner;
    private User currentUser;

    public MenuController(Scanner scanner) {
        this.scanner = scanner;
        RegisterMenuController registerController = new RegisterMenuController(scanner);
        this.currentMenu = new RegisterMenu(this, registerController, scanner);
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

        if ((currentMenuName.equals("Main Menu") && target == Menu.GAME) ||
                (currentMenuName.equals("Main Menu") && target == Menu.PROFILE) ||
                (currentMenuName.equals("Main Menu") && target == Menu.AVATAR)){
            return true;
        }

        if ((currentMenuName.equals("Login Menu") && target == Menu.REGISTER) ||
                (currentMenuName.equals("Register Menu") && target == Menu.LOGIN)) {
            return true;
        }

        return false;
    }

    private AppMenu createMenuInstance(Menu menu) {
        switch (menu) {
            case LOGIN:
                LoginMenuController loginMenuController = new LoginMenuController(scanner);
                return new LoginMenu(this, loginMenuController, scanner);
            case MAIN: return new MainMenu(this);
            case PROFILE:
                ProfileMenuController profileController = new ProfileMenuController(
                        this.getScanner(),
                        this.currentUser
                );
                return new ProfileMenu(this, profileController);
            case GAME: return new GameMenu(this, new GameMenuController(this.scanner, this.currentUser));
            case REGISTER:
                RegisterMenuController registerController = new RegisterMenuController(this.getScanner());
                return new RegisterMenu(this, registerController, scanner);
            case TRADE:
                return new TradeMenu(new TradingController(), scanner);
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
                System.out.println(showCurrentMenu().getMessage());
            }
            if (input.equals("exit") && (currentMenu.getMenuName().equals("login menu") || currentMenu.getMenuName().equals("register menu")))
                break;
            else {
                currentMenu.handleUserInput(input);
            }
        }
    }

    public Result logoutUser() {
        if (currentMenu.getMenuName().equals("Main Menu")) {
            currentMenu = createMenuInstance(Menu.LOGIN);
            return new Result(true, "Logged out successfully! Redirected to login menu.");
        } else {
            return new Result(false, "You can only logout from Main Menu!");
        }
    }
    public Scanner getScanner() {
        return scanner;
    }
}