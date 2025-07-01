package org.example.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.controllers.GameMenuController;
import org.example.controllers.ProfileMenuController;
import org.example.controllers.RegisterMenuController;
import org.example.views.*;

import java.util.List;

public class MenuNavigator {
    private static Game game;
    private static Skin sharedSkin;
    private static LoginMenu loginMenu;
    private static RegisterMenu registerMenu;
    private static MainMenu mainMenu;
    private static ProfileMenu profileMenu;
    private static GameMenu gameMenuScreen;
    private static MapSelectionScreen mapSelectionScreen;

    public static void init(Game gameRef, Skin skin) {
        game = gameRef;
        sharedSkin = skin;
        loginMenu = new LoginMenu(sharedSkin);
        registerMenu = new RegisterMenu(sharedSkin);
        mainMenu = new MainMenu(sharedSkin);
    }

    public static void showProfileMenu() {
        if (profileMenu == null) {
            profileMenu = new ProfileMenu(sharedSkin, new ProfileMenuController(RegisterMenuController.currentUser));
        }
        game.setScreen(profileMenu);
    }

    public static void showMapSelectionScreen(List<String> usernames, Skin skin, GameMenuController controller) {
        mapSelectionScreen = new MapSelectionScreen(usernames, skin, controller);
        game.setScreen(mapSelectionScreen);
    }

    public static void showGameExitConfirmation(Skin skin) {
        game.setScreen(new GameExitConfirmationScreen(skin));
    }


    public static void showGameMenu() {
        if (gameMenuScreen == null) {
            gameMenuScreen = new GameMenu(sharedSkin, new GameMenuController(RegisterMenuController.currentUser));
        }
        game.setScreen(gameMenuScreen);
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
