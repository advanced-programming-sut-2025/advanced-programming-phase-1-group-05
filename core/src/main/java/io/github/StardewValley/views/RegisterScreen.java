package io.github.StardewValley.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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

        // 👇 تغییر فقط در بارگذاری اسکین
        skin = GameAssetManager.getInstance().getSkin();
        bgTexture = new Texture(Gdx.files.internal("backgrounds/register_bg.png"));

        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        stage.addActor(table);

        usernameField = new TextField("", skin);
        table.add(new Label("Username:", skin)).pad(5);
        table.add(usernameField).width(200).pad(5).row();

        nicknameField = new TextField("", skin);
        table.add(new Label("Nickname:", skin)).pad(5);
        table.add(nicknameField).width(200).pad(5).row();

        emailField = new TextField("", skin);
        table.add(new Label("Email:", skin)).pad(5);
        table.add(emailField).width(200).pad(5).row();

        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        table.add(new Label("Password:", skin)).pad(5);
        table.add(passwordField).width(200).pad(5).row();

        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        table.add(new Label("Confirm Password:", skin)).pad(5);
        table.add(confirmPasswordField).width(200).pad(5).row();

        genderBox = new SelectBox<String>(skin);
        genderBox.setItems("Male", "Female");
        table.add(new Label("Gender:", skin)).pad(5);
        table.add(genderBox).width(200).pad(5).row();

        TextButton randomButton = new TextButton("Generate Random Password", skin);
        table.add(randomButton).colspan(2).pad(5).row();

        errorLabel = new Label("", skin);
        table.add(errorLabel).colspan(2).pad(5).row();

        TextButton registerButton = new TextButton("Register", skin);
        TextButton backButton = new TextButton("Back to Login", skin);
        table.add(registerButton).pad(5);
        table.add(backButton).pad(5).row();

        randomButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String randomPass = "";
                for (int i = 0; i < 8; i++) {
                    char c = (char)('a' + (int)(Math.random() * 26));
                    randomPass += c;
                }
                passwordField.setText(randomPass);
                confirmPasswordField.setText(randomPass);
            }
        });

        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!questionVisible) {
                    String user = usernameField.getText().trim();
                    String nick = nicknameField.getText().trim();
                    String email = emailField.getText().trim();
                    String pass = passwordField.getText();
                    String confirmPass = confirmPasswordField.getText();
                    if (user.isEmpty() || nick.isEmpty() || email.isEmpty() || pass.isEmpty() || !pass.equals(confirmPass)) {
                        errorLabel.setText("Please fill all fields correctly");
                    } else {
                        questionVisible = true;
                        table.clear();
                        table.setFillParent(true);
                        table.setBackground(new TextureRegionDrawable(new TextureRegion(bgTexture)));

                        table.add(new Label("Username:", skin)).pad(5);
                        table.add(usernameField).width(200).pad(5).row();
                        table.add(new Label("Nickname:", skin)).pad(5);
                        table.add(nicknameField).width(200).pad(5).row();
                        table.add(new Label("Email:", skin)).pad(5);
                        table.add(emailField).width(200).pad(5).row();
                        table.add(new Label("Password:", skin)).pad(5);
                        table.add(passwordField).width(200).pad(5).row();
                        table.add(new Label("Confirm Password:", skin)).pad(5);
                        table.add(confirmPasswordField).width(200).pad(5).row();
                        table.add(new Label("Gender:", skin)).pad(5);
                        table.add(genderBox).width(200).pad(5).row();
                        table.add(randomButton).colspan(2).pad(5).row();
                        table.add(errorLabel).colspan(2).pad(5).row();

                        Label questionLabel = new Label("Security Question:", skin);
                        questionBox = new SelectBox<String>(skin);
                        questionBox.setItems("Your pet's name?", "Your birth city?", "Favorite color?");
                        table.add(questionLabel).pad(5);
                        table.add(questionBox).width(200).pad(5).row();
                        Label ansLabel = new Label("Answer:", skin);
                        answerField = new TextField("", skin);
                        table.add(ansLabel).pad(5);
                        table.add(answerField).width(200).pad(5).row();
                        Label confirmAnsLabel = new Label("Confirm Answer:", skin);
                        confirmAnswerField = new TextField("", skin);
                        table.add(confirmAnsLabel).pad(5);
                        table.add(confirmAnswerField).width(200).pad(5).row();

                        table.add(registerButton).pad(5);
                        table.add(backButton).pad(5).row();
                    }
                } else {
                    String ans = answerField.getText().trim();
                    String ansConfirm = confirmAnswerField.getText().trim();
                    if (ans.isEmpty() || !ans.equals(ansConfirm)) {
                        errorLabel.setText("Answers do not match or are empty");
                    } else {
                        game.setScreen(new MainMenuScreen(game));
                    }
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new LoginScreen(game));
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
