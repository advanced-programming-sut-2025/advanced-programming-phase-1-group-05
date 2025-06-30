package io.github.StardewValley.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.StardewValley.models.GameAssetManager;

public class RegisterScreen implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private Texture bgTexture;
    private TextField usernameField, nicknameField, emailField;
    private TextField passwordField, confirmPasswordField;
    private TextField answerField, confirmAnswerField;
    private SelectBox<String> genderBox, questionBox;
    private Label errorLabel;
    private boolean questionVisible = false;

    public RegisterScreen(Game game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new FitViewport(800, 600));
        Gdx.input.setInputProcessor(stage);

        skin = GameAssetManager.getInstance().getSkin();
        bgTexture = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        stage.addActor(table);

        Label title = new Label("REGISTER", skin);
        title.setFontScale(0.9f);
        title.setColor(0.8f, 0.6f, 0.1f, 1); // طلایی
        table.top().padTop(0); // از لوگین روی پس‌زمینه فاصله می‌گیریم
        table.add(title).colspan(2).padBottom(20).row();

        usernameField = new TextField("", skin);
        usernameField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Username:", skin)).pad(5);
        table.add(usernameField).width(200).pad(5).row();

        nicknameField = new TextField("", skin);
        nicknameField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Nickname:", skin)).pad(5);
        table.add(nicknameField).width(200).pad(5).row();

        emailField = new TextField("", skin);
        emailField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Email:", skin)).pad(5);
        table.add(emailField).width(200).pad(5).row();

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        passwordField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Password:", skin)).pad(5);
        table.add(passwordField).width(200).pad(5).row();

        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        confirmPasswordField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Confirm Password:", skin)).pad(5);
        table.add(confirmPasswordField).width(200).pad(5).row();

        genderBox = new SelectBox<>(skin);
        genderBox.setItems("Male", "Female");
        table.add(new Label("Gender:", skin)).pad(5);
        table.add(genderBox).width(200).pad(5).row();

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(0.5f);
        registerButton.getStyle().fontColor = new Label.LabelStyle().fontColor.valueOf("#FFD700"); // طلایی

        table.add(registerButton).colspan(2).padTop(15);
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
