package org.example.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.views.*;

public class MenuNavigator {
    private static Game game;
    private static Skin sharedSkin;
    private static LoginMenu loginMenu;
    private static RegisterMenu registerMenu;
    private static MainMenu mainMenu;

    public static void init(Game gameRef, Skin skin) {
        game = gameRef;
        sharedSkin = skin;
        loginMenu = new LoginMenu(sharedSkin);
//        registerMenu = new RegisterMenu(sharedSkin);
        mainMenu = new MainMenu(sharedSkin);
    }

    public static void showLoginMenu() { game.setScreen(loginMenu); }
    public static void showRegisterMenu() { game.setScreen(registerMenu); }
    public static void showMainMenu() { game.setScreen(mainMenu); }
}
