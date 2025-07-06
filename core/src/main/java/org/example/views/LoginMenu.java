package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.example.controllers.LoginMenuController;
import org.example.models.*;

public class LoginMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private TextField usernameField, passwordField;
    private CheckBox stayLoggedCheck;
    private Label messageLabel;
    private final LoginMenuController controller;

    public LoginMenu(Skin skin) {
        this.skin = skin;
        this.controller = new LoginMenuController();
        this.stage = new Stage(new FitViewport(800, 600));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        stage.setKeyboardFocus(null); // پاک کردن فوکوس قبلی

        Texture bg = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));
        stage.addActor(root);

        Label title = new Label("LOGIN", skin, "title");
        messageLabel = new Label("", skin);
        messageLabel.setWrap(true);
        messageLabel.setColor(Color.RED);
        messageLabel.setFontScale(0.8f);

        root.add(title).colspan(2).padBottom(10).row();
        root.add(messageLabel).colspan(2).width(400).padBottom(10).row();

        usernameField = new TextField("", skin);
        usernameField.setTouchable(Touchable.enabled);
        usernameField.setFocusTraversal(true);

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.setTouchable(Touchable.enabled);
        passwordField.setFocusTraversal(true);

        stayLoggedCheck = new CheckBox("Stay logged in", skin);

        root.add(new Label("Username:", skin)).pad(5);
        root.add(usernameField).width(200).pad(5).row();
        root.add(new Label("Password:", skin)).pad(5);
        root.add(passwordField).width(200).pad(5).row();
        root.add(stayLoggedCheck).colspan(2).left().pad(5).row();

        TextButton loginBtn = new TextButton("Login", skin);
        TextButton registerBtn = new TextButton("Register", skin);
        TextButton forgotBtn = new TextButton("Forgot Password", skin);

        root.add(loginBtn).pad(5);
        root.add(registerBtn).pad(5).row();
        root.add(forgotBtn).colspan(2).pad(5).row();

        // فوکوس روی فیلد اول
        stage.setKeyboardFocus(usernameField);

        loginBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String user = usernameField.getText().trim();
                String pass = passwordField.getText().trim();
                boolean stayLogged = stayLoggedCheck.isChecked();

                Result result = controller.loginUser(user, pass, stayLogged);
                if (result.isSuccess()) {
                    messageLabel.setColor(Color.GREEN);
                    messageLabel.setText("Welcome, " + user + "!");
                    MenuNavigator.showMainMenu();
                } else {
                    messageLabel.setColor(Color.RED);
                    messageLabel.setText(result.getMessage());
                }
            }
        });

        registerBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.setSkin(skin);
                MenuNavigator.showRegisterMenu();
            }
        });

        forgotBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showRecoveryDialog();
            }
        });
    }


    private void showRecoveryDialog() {
        Dialog dialog = new Dialog("Recover Password", skin);

        TextField username = new TextField("", skin);
        TextField answer = new TextField("", skin);
        Label result = new Label("", skin);
        TextButton copyBtn = new TextButton("Copy", skin);
        copyBtn.setVisible(false);

        Table content = dialog.getContentTable();
        content.add("Username:").pad(3);
        content.add(username).width(200).pad(3).row();
        content.add("Answer:").pad(3);
        content.add(answer).width(200).pad(3).row();
        content.add(result).colspan(2).pad(5).row();
        content.add(copyBtn).colspan(2).pad(5).row();

        dialog.button("OK");
        dialog.button("Check").addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                User user = UserDatabase.getUserByUsername(username.getText().trim());
                if (user != null && user.getSecurityAnswer().equals(answer.getText().trim())) {
                    result.setText("Password: " + user.getPlainPassword());
                    copyBtn.setVisible(true);
                } else {
                    result.setText("Incorrect answer");
                    copyBtn.setVisible(false);
                }
            }
        });

        copyBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.getClipboard().setContents(result.getText().toString().replace("Password: ", ""));
                result.setText("Copied to clipboard");
            }
        });

        dialog.show(stage);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
}
