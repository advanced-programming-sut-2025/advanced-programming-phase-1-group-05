package org.example.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.example.models.GameAssetManager;

public class LoginMenu implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private Texture bgTexture;
    private TextField usernameField, passwordField;
    private CheckBox stayLoggedCheck;
    private Label errorLabel;

    public LoginMenu(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        skin = GameAssetManager.getInstance().getSkin();
        bgTexture = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        stage.addActor(rootTable);

        // 🪧 لوگو بالا
        Label title = new Label("LOGIN", skin, "title");
        Table logoTable = new Table();
        logoTable.add(title).padTop(50);
        rootTable.add(logoTable).expandX().top().padTop(50).row();

        // 🧾 فرم ورود
        Table formTable = new Table();

        usernameField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        formTable.add(new Label("Username:", skin, "subtitle")).right().pad(5);
        formTable.add(usernameField).width(200).pad(5).row();

        formTable.add(new Label("Password:", skin, "subtitle")).right().pad(5);
        formTable.add(passwordField).width(200).pad(5).row();

        stayLoggedCheck = new CheckBox("Stay logged in", skin);
        formTable.add(stayLoggedCheck).colspan(2).pad(5).left().row();

        rootTable.add(formTable).expand().center().row();

        // 🎮 دکمه‌ها
        Table buttonTable = new Table();
        TextButton loginButton = new TextButton("Login", skin);
        TextButton registerButton = new TextButton("Register", skin);
        TextButton forgotButton = new TextButton("Forgot Password", skin);
        errorLabel = new Label("", skin, "subtitle");

        buttonTable.add(loginButton).pad(5);
        buttonTable.add(registerButton).pad(5).row();
        buttonTable.add(forgotButton).colspan(2).pad(5).row();
        buttonTable.add(errorLabel).colspan(2).pad(5).row();

        rootTable.add(buttonTable).bottom().padBottom(40);


        loginButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String user = usernameField.getText().trim();
                String pass = passwordField.getText().trim();
                if (user.isEmpty() || pass.isEmpty()) {
                    errorLabel.setText("Please enter username and password");
                } else {
                    if (user.equals("user") && pass.equals("pass")) {
//                        game.setScreen(new MainMenuScreen(game));
                    } else {
                        errorLabel.setText("Invalid credentials");
                    }
                }
            }
        });

        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new RegisterMenu(game));
            }
        });

        forgotButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                errorLabel.setText("Forgot Password clicked");
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

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
        bgTexture.dispose();
    }
}
