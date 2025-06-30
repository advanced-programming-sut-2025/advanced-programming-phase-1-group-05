//package org.example.views;
//
//import com.badlogic.gdx.Game;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.FitViewport;
//import org.example.controllers.LoginMenuController;
//import org.example.models.GameAssetManager;
//
//public class LoginScreen implements Screen {
//    private final Game game;
//    private final Stage stage;
//    private final Skin skin;
//    private final LoginMenuController controller;
//
//    private Texture bgTexture;
//    private TextField usernameField, passwordField;
//    private CheckBox stayLoggedCheck;
//    private Label errorLabel;
//
//    public LoginScreen(Game game) {
//        this.game = game;
//        this.skin = GameAssetManager.getInstance().getSkin();
//        this.controller = new LoginMenuController();
//        this.stage = new Stage(new FitViewport(800, 600));
//    }
//
//    @Override
//    public void show() {
//        Gdx.input.setInputProcessor(stage);
//
//        bgTexture = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));
//
//        Table rootTable = new Table();
//        rootTable.setFillParent(true);
//        rootTable.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
//        stage.addActor(rootTable);
//
//        Label title = new Label("LOGIN", skin, "title");
//        rootTable.add(title).expandX().top().padTop(50).row();
//
//        Table formTable = new Table();
//
//        usernameField = new TextField("", skin);
//        passwordField = new TextField("", skin);
//        passwordField.setPasswordMode(true);
//        passwordField.setPasswordCharacter('*');
//
//        formTable.add(new Label("Username:", skin, "subtitle")).right().pad(5);
//        formTable.add(usernameField).width(200).pad(5).row();
//
//        formTable.add(new Label("Password:", skin, "subtitle")).right().pad(5);
//        formTable.add(passwordField).width(200).pad(5).row();
//
//        stayLoggedCheck = new CheckBox("Stay logged in", skin);
//        formTable.add(stayLoggedCheck).colspan(2).pad(5).left().row();
//
//        rootTable.add(formTable).expand().center().row();
//
//        Table buttonTable = new Table();
//        TextButton loginButton = new TextButton("Login", skin);
//        TextButton registerButton = new TextButton("Register", skin);
//        TextButton forgotButton = new TextButton("Forgot Password", skin);
//        errorLabel = new Label("", skin, "subtitle");
//
//        buttonTable.add(loginButton).pad(5);
//        buttonTable.add(registerButton).pad(5).row();
//        buttonTable.add(forgotButton).colspan(2).pad(5).row();
//        buttonTable.add(errorLabel).colspan(2).pad(5).row();
//
//        rootTable.add(buttonTable).bottom().padBottom(40);
//
//        loginButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                String user = usernameField.getText().trim();
//                String pass = passwordField.getText().trim();
//                if (controller.login(user, pass)) {
//                    game.setScreen(new MainMenuScreen(game));
//                } else {
//                    errorLabel.setText("Invalid credentials");
//                }
//            }
//        });
//
//        registerButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                game.setScreen(new RegisterScreen(game));
//            }
//        });
//
//        forgotButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                errorLabel.setText("Forgot Password clicked");
//            }
//        });
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        stage.act(delta);
//        stage.draw();
//    }
//
//    @Override public void resize(int width, int height) {
//        stage.getViewport().update(width, height, true);
//    }
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//    @Override public void dispose() {
//        stage.dispose();
//        bgTexture.dispose();
//    }
//}

//***
//package org.example.views;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import org.example.controllers.LoginMenuController;
//
//public class LoginMenu implements Screen {
//    private final Stage stage;
//    private final Skin skin;
//    private TextField usernameField;
//    private TextField passwordField;
//    private Label messageLabel;
//    private CheckBox stayLoggedCheck;
//    private Texture bgTexture;
//    private Label errorLabel;
//    private final LoginMenuController controller;
//
//    public LoginMenu(Skin skin, LoginMenuController controller) {
//        this.skin = skin;
//        this.controller = controller;
//        this.stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//
//        Table table = new Table();
//        table.setFillParent(true);
//
//        usernameField = new TextField("", skin);
//        passwordField = new TextField("", skin);
//        passwordField.setPasswordMode(true);
//        passwordField.setPasswordCharacter('*');
//
//        TextButton loginButton = new TextButton("Login", skin);
//        loginButton.setWidth(150);
//        loginButton.setHeight(50);
//        TextButton registerButton = new TextButton("Register", skin);
//        registerButton.setWidth(150);
//        registerButton.setHeight(50);
//
//        messageLabel = new Label("", skin);
//
//        table.add(new Label("Username", skin)).row();
//        table.add(usernameField).width(150).height(70).padBottom(3).row();
//        table.add(new Label("Password", skin)).row();
//        table.add(passwordField).width(150).height(70).padBottom(3).row();
//        table.add(loginButton).padTop(10).row();
//        table.add(registerButton).padTop(5).row();
//        table.add(messageLabel).padTop(10).row();
//
//        stage.addActor(table);
//
//        loginButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                String user = usernameField.getText().trim();
//                String pass = passwordField.getText().trim();
//                if (user.equals("admin") && pass.equals("1234")) {
//                    messageLabel.setText("Login successful!");
//                    MenuNavigator.showMainMenu();
//                } else {
//                    messageLabel.setText("Invalid credentials");
//                }
//            }
//        });
//
//        registerButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                MenuNavigator.showRegisterMenu();
//            }
//        });
//    }
//
////    @Override
////    public void show() {
////        Gdx.input.setInputProcessor(stage);
////    }
//    @Override
//    public void show() {
//        Gdx.input.setInputProcessor(stage);
//
//        bgTexture = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));
//
//        Table rootTable = new Table();
//        rootTable.setFillParent(true);
//        rootTable.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
//        stage.addActor(rootTable);
//
//        Label title = new Label("LOGIN", skin, "title");
//        rootTable.add(title).expandX().top().padTop(50).row();
//
//        Table formTable = new Table();
//
//        usernameField = new TextField("", skin);
//        passwordField = new TextField("", skin);
//        passwordField.setPasswordMode(true);
//        passwordField.setPasswordCharacter('*');
//
//        formTable.add(new Label("Username:", skin, "subtitle")).right().pad(5);
//        formTable.add(usernameField).width(200).pad(5).row();
//
//        formTable.add(new Label("Password:", skin, "subtitle")).right().pad(5);
//        formTable.add(passwordField).width(200).pad(5).row();
//
//        stayLoggedCheck = new CheckBox("Stay logged in", skin);
//        formTable.add(stayLoggedCheck).colspan(2).pad(5).left().row();
//
//        rootTable.add(formTable).expand().center().row();
//
//        Table buttonTable = new Table();
//        TextButton loginButton = new TextButton("Login", skin);
//        TextButton registerButton = new TextButton("Register", skin);
//        TextButton forgotButton = new TextButton("Forgot Password", skin);
//        errorLabel = new Label("", skin, "subtitle");
//
//        buttonTable.add(loginButton).pad(5);
//        buttonTable.add(registerButton).pad(5).row();
//        buttonTable.add(forgotButton).colspan(2).pad(5).row();
//        buttonTable.add(errorLabel).colspan(2).pad(5).row();
//
//        rootTable.add(buttonTable).bottom().padBottom(40);
//
//        loginButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                String user = usernameField.getText().trim();
//                String pass = passwordField.getText().trim();
////                if (controller.loginUser(user, pass , )) {
////                    game.setScreen(new MainMenu(skin));
////                } else {
////                    errorLabel.setText("Invalid credentials");
////                }
//            }
//        });
//
//        registerButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
////                game.setScreen(new RegisterMenu(skin));
//            }
//        });
//
//        forgotButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                errorLabel.setText("Forgot Password clicked");
//            }
//        });
//    }
//
//    @Override public void render(float delta) {
//        Gdx.gl.glClearColor(0,0,0,1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        stage.act();
//        stage.draw();
//    }
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//    @Override public void dispose() { stage.dispose(); }
//}


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
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();
                Result result = controller.loginUser(username, password, false);
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
