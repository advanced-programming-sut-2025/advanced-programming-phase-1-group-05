package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.LoginMenuController;
import org.example.models.Result;

public class LoginMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final LoginMenuController controller;
    CheckBox stayLoggedCheck;

    private TextField usernameField;
    private TextField passwordField;
    private Label messageLabel;

    public LoginMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        this.controller = new LoginMenuController();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label title = new Label("Login", skin, "title");
        root.add(title).colspan(2).padBottom(20).row();

        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        root.add(new Label("Username:", skin)).right().pad(5);
        root.add(usernameField).width(200).pad(5).row();

        root.add(new Label("Password:", skin)).right().pad(5);
        root.add(passwordField).width(200).pad(5).row();

        stayLoggedCheck = new CheckBox("Stay logged in", skin);
        root.add(stayLoggedCheck).colspan(2).pad(10).row();

        messageLabel = new Label("", skin);
        root.add(messageLabel).colspan(2).pad(10).row();

        TextButton loginButton = new TextButton("Login", skin);
        TextButton registerButton = new TextButton("Register", skin);
        TextButton forgotButton = new TextButton("Forgot Password", skin);

        root.add(loginButton).pad(5);
        root.add(registerButton).pad(5).row();
        root.add(forgotButton).colspan(2).padTop(10).row();

        // Login handler
        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                boolean stayLogged = stayLoggedCheck.isChecked();

                Result result = controller.loginUser(username, password, stayLogged);
                if (result.isSuccess()) {
                    messageLabel.setText("Welcome, " + username + "!");
                    MenuNavigator.showMainMenu();
                } else {
                    messageLabel.setText(result.getMessage());
                }
            }
        });

        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showRegisterMenu();
            }
        });

        forgotButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                messageLabel.setText("Feature not implemented.");
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
