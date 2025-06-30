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
//import org.example.models.GameAssetManager;
//
//public class RegisterMenu implements Screen {
//    private Game game;
//    private Stage stage;
//    private Skin skin;
//    private Texture bgTexture;
//    private TextField usernameField, nicknameField, emailField;
//    private TextField passwordField, confirmPasswordField;
//    private TextField answerField, confirmAnswerField;
//    private SelectBox<String> genderBox, questionBox;
//    private Label errorLabel;
//
//    public RegisterMenu(Game game) {
//        this.game = game;
//    }
//
//    @Override
//    public void show() {
//        // ساخت Stage و تنظیم ورودی
//        stage = new Stage(new FitViewport(800, 600));
//        Gdx.input.setInputProcessor(stage);
//
//        skin = GameAssetManager.getInstance().getSkin();
//        bgTexture = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));
//
//        Table table = new Table();
//        table.setFillParent(true);
//        table.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
//        stage.addActor(table);
//
//        // عنوان REGISTER
//        Label title = new Label("REGISTER", skin);
//        title.setFontScale(0.9f);
//        title.setColor(0.8f, 0.6f, 0.1f, 1); // طلایی
//        table.top().padTop(0);
//        table.add(title).colspan(2).padBottom(20).row();
//
//        // فیلدهای فرم ثبت‌نام
//        usernameField = new TextField("", skin);
//        table.add(new Label("Username:", skin)).pad(5);
//        table.add(usernameField).width(200).pad(5).row();
//
//        nicknameField = new TextField("", skin);
//        table.add(new Label("Nickname:", skin)).pad(5);
//        table.add(nicknameField).width(200).pad(5).row();
//
//        emailField = new TextField("", skin);
//        table.add(new Label("Email:", skin)).pad(5);
//        table.add(emailField).width(200).pad(5).row();
//
//        passwordField = new TextField("", skin);
//        passwordField.setPasswordMode(true);
//        passwordField.setPasswordCharacter('*');
//        table.add(new Label("Password:", skin)).pad(5);
//        table.add(passwordField).width(200).pad(5).row();
//
//        confirmPasswordField = new TextField("", skin);
//        confirmPasswordField.setPasswordMode(true);
//        confirmPasswordField.setPasswordCharacter('*');
//        table.add(new Label("Confirm Password:", skin)).pad(5);
//        table.add(confirmPasswordField).width(200).pad(5).row();
//
//        genderBox = new SelectBox<>(skin);
//        genderBox.setItems("Male", "Female");
//        table.add(new Label("Gender:", skin)).pad(5);
//        table.add(genderBox).width(200).pad(5).row();
//
//        // دکمه‌های ثبت و بازگشت
//        TextButton registerButton = new TextButton("Register", skin);
//        TextButton backButton = new TextButton("Back", skin);
//        table.add(registerButton).colspan(2).padTop(15).row();
//        table.add(backButton).colspan(2).padTop(5);
//
//        // لیسنر دکمه Register
//        registerButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                // اینجا می‌توان ثبت‌نام را انجام داد؛ پس از موفقیت به منوی اصلی می‌رویم
//                game.setScreen(new MainMenu(game));
//            }
//        });
//
//        // لیسنر دکمه Back
//        backButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                game.setScreen(new LoginMenu(game));
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
// RegisterMenu.java
package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.RegisterMenuController;

public class RegisterMenu implements Screen {
    private final Stage stage;
    private final Skin skin;

    public RegisterMenu(RegisterMenuController registerMenuController,Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);

        TextField username = new TextField("", skin);
        TextField password = new TextField("", skin);
        password.setPasswordMode(true);
        password.setPasswordCharacter('*');

        TextButton submitButton = new TextButton("Register", skin);
        TextButton backButton = new TextButton("Back", skin);

        table.add(new Label("Register Username", skin)).row();
        table.add(username).width(200).padBottom(10).row();
        table.add(new Label("Register Password", skin)).row();
        table.add(password).width(200).padBottom(10).row();
        table.add(submitButton).padTop(10).row();
        table.add(backButton).padTop(5).row();

        stage.addActor(table);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showMainMenu();
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
