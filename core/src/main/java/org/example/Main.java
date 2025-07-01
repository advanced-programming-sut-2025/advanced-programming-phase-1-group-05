// Main.java
package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.controllers.RegisterMenuController;
import org.example.models.User;
import org.example.models.UserDatabase;
import org.example.views.MenuNavigator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends Game {
    public static SpriteBatch batch;
    private Skin skin;
    private static Main main;

    public static Main getMain() {
        return main;
    }

    public static void setBatch(SpriteBatch batch) {
        Main.batch = batch;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
        MenuNavigator.init(this, skin);
        checkAutoLogin();
//        MenuNavigator.showLoginMenu();
    }

    public static void checkAutoLogin() {
        UserDatabase.loadUsers();
        File file = new File("currentuser.json");
        if (!file.exists()) {
            MenuNavigator.showLoginMenu();
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get("currentuser.json")));
            String username = extractValue(content, "username");
            String stayLoggedStr = extractValue(content, "stayLogged");

            boolean stayLogged = Boolean.parseBoolean(stayLoggedStr);

            if (stayLogged && username != null) {
                User user = UserDatabase.getUserByUsername(username);
                if (user != null) {
                    RegisterMenuController.currentUser = user;
                    MenuNavigator.showMainMenu();
                    return;
                }
            }else {
                MenuNavigator.showLoginMenu();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        MenuNavigator.showLoginMenu();
    }

    private static String extractValue(String json, String key) {
        // بسیار ساده و فقط برای فایل JSON کوچک با فرمت استاندارد!
        String pattern = "\"" + key + "\"\\s*:\\s*";
        int index = json.indexOf("\"" + key + "\"");
        if (index == -1) return null;

        int colonIndex = json.indexOf(":", index);
        int startQuote = json.indexOf("\"", colonIndex + 1);
        int endQuote = json.indexOf("\"", startQuote + 1);

        if (startQuote == -1 || endQuote == -1) {
            // اگر مقدار boolean باشه (بدون " ")
            String part = json.substring(colonIndex + 1).trim();
            if (part.startsWith("true")) return "true";
            if (part.startsWith("false")) return "false";
            return null;
        }

        return json.substring(startQuote + 1, endQuote);
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        skin.dispose();
    }
}
