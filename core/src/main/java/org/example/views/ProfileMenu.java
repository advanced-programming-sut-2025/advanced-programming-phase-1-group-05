package org.example.views;

import org.example.controllers.MenuController;
import org.example.controllers.ProfileMenuController;
import org.example.models.Result;

public class ProfileMenu implements org.example.views.AppMenu {
    private final MenuController menuController;
    private final ProfileMenuController profileController;

    public ProfileMenu(MenuController menuController, ProfileMenuController profileController) {
        this.menuController = menuController;
        this.profileController = profileController;
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
        }
        else if (input.equalsIgnoreCase("user logout")){
            Result result = menuController.logoutUser();
            System.out.println(result.getMessage());
        } else if (input.equals("show menus")) {
            menuController.showMenus();
        } else {
            Result result = profileController.handleProfileCommand(input);
            System.out.println(result.getMessage());
        }
    }
    @Override
    public String getMenuName() {
        return "Profile Menu";
    }
}