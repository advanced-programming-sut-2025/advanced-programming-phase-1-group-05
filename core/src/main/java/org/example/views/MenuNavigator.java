package org.example.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.controllers.ProfileMenuController;
import org.example.controllers.RegisterMenuController;
import org.example.views.*;

public class MenuNavigator {
    private static Game game;
    private static Skin sharedSkin;
    private static LoginMenu loginMenu;
    private static RegisterMenu registerMenu;
    private static MainMenu mainMenu;
    private static ProfileMenu profileMenu;

    public static void init(Game gameRef, Skin skin) {
        game = gameRef;
        sharedSkin = skin;
        loginMenu = new LoginMenu(sharedSkin);
        registerMenu = new RegisterMenu(sharedSkin);
        mainMenu = new MainMenu(sharedSkin);
//        profileMenu = new ProfileMenu(sharedSkin , new ProfileMenuController(RegisterMenuController.currentUser));
    }

    public static void showProfileMenu() {
        if (RegisterMenuController.currentUser == null) {
            System.out.println("کاربر لاگین نشده و profileMenu قابل ساخت نیست");
            showLoginMenu();
            return;
        }

        if (profileMenu == null) {
            profileMenu = new ProfileMenu(sharedSkin, new ProfileMenuController(RegisterMenuController.currentUser));
        }

        game.setScreen(profileMenu);
    }

    public static void showLoginMenu() { game.setScreen(loginMenu); }
    public static void showRegisterMenu() { game.setScreen(registerMenu); }
    public static void showMainMenu() { game.setScreen(mainMenu); }
    public static void setSkin(Skin sharedSkin) {
        MenuNavigator.sharedSkin = sharedSkin;
    }

    public static Skin getSharedSkin() {
        return sharedSkin;
    }
}
