package org.example.controllers;

import org.example.models.Game;
import org.example.models.Result;
import org.example.models.Enums.Menu;
import org.example.models.User;
import org.example.views.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class MenuController {
    private static MenuController menuController;
    private org.example.views.AppMenu currentMenu;
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
        if ((currentMenuName.equals("Main Menu") && target == Menu.GAME) ||
                (currentMenuName.equals("Main Menu") && target == Menu.PROFILE) ||
                (currentMenuName.equals("Main Menu") && target == Menu.AVATAR)||
                (currentMenuName.equals("Main Menu") && target == Menu.LOGIN) ||
                (currentMenuName.equals("Main Menu") && target == Menu.REGISTER) ||
                (currentMenuName.equals("Game Menu") && target == Menu.HOME) ||
                (currentMenuName.equals("Home Menu") && target == Menu.GAME)||
                (currentMenuName.equals("Game Menu") && target == Menu.TRADE) ||
                (currentMenuName.equals("trade menu") && target == Menu.GAME)
        ) {
            return true;
        }

        if ((currentMenuName.equals("Login Menu") && target == Menu.REGISTER) ||
                (currentMenuName.equals("Register Menu") && target == Menu.LOGIN)) {
            return true;
        }
        else {
            return false;
        }

//        return false;
    }

    private AppMenu createMenuInstance(Menu menu) {
        switch (menu) {
            case LOGIN:
                LoginMenuController loginController = new LoginMenuController(this.getScanner());
                return new LoginMenu(this, loginController, this.getScanner());
            case MAIN: return new MainMenu(this);
            case PROFILE:
                ProfileMenuController profileController = new ProfileMenuController(
                        this.getScanner(),
                        LoginMenuController.currentUser
                );
                return new ProfileMenu(this, profileController);
            case GAME:
                GameMenuController gameMenuController = new GameMenuController(
                        this.scanner,
                        LoginMenuController.currentUser
                );
                return new GameMenu(this ,gameMenuController);
            case REGISTER:
                RegisterMenuController registerController = new RegisterMenuController(this.getScanner());
                return new RegisterMenu(this, registerController, this.getScanner());
            case TRADE: {
                return new TradeMenu(TradingController.getInstance(), this);
            }
            case HOME: {
                return new HomeMenu(this);
            }

            default: throw new IllegalArgumentException("Unknown menu type");
        }
    }

    public Result exitMenu() {
        System.out.println("Exiting app...");
        return new Result(true, "App exited successfully");
    }

    public Result showCurrentMenu() {
        return new Result(true, "Current menu: " + currentMenu.getMenuName());
    }

    public void run() throws IOException {
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

    public void showMenus() {
        System.out.println("Register\nLogin\nMain\nProfile\nGame\nAvatar\nHome\nTrade");
    }
}