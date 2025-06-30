package org.example.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.LoginMenuController;
import org.example.controllers.RegisterMenuController;
import org.example.models.Result;
import org.example.views.LoginMenu;
import org.example.models.GameAssetManager;

public class RegisterMenu implements Screen {
    private Game game;
    private Stage stage;
    private Skin skin;
    private Texture bgTexture;
    private TextField usernameField, nicknameField, emailField;
    private TextField passwordField, confirmPasswordField;
    private TextField answerField, confirmAnswerField;
    private SelectBox<String> genderBox, questionBox;
    private Label errorLabel;
    private final RegisterMenuController controller;

    public RegisterMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new ScreenViewport());
        this.controller = new RegisterMenuController();
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
        table.top().padTop(0);
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

        // فیلدهای سؤال امنیتی
        questionBox = new SelectBox<>(skin);
        questionBox.setItems(
            "What was your first pet's name?",
            "What city were you born in?",
            "What is your favorite color?"
        );

        table.add(new Label("Security Question:", skin)).pad(5);
        table.add(questionBox).width(300).pad(5).row();

        answerField = new TextField("", skin);
        answerField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Answer:", skin)).pad(5);
        table.add(answerField).width(200).pad(5).row();

        confirmAnswerField = new TextField("", skin);
        confirmAnswerField.getStyle().font.getData().setScale(0.5f);
        table.add(new Label("Confirm Answer:", skin)).pad(5);
        table.add(confirmAnswerField).width(200).pad(5).row();

        TextButton registerButton = new TextButton("Register", skin);
        registerButton.getLabel().setFontScale(0.5f);
        registerButton.getStyle().fontColor = Color.valueOf("#FFD700"); // طلایی

        table.add(registerButton).colspan(2).padTop(15);

        // برچسب خطا
        errorLabel = new Label("", skin);
        errorLabel.setColor(Color.RED);
        table.add(errorLabel).colspan(2).padTop(10).row();

        // Listener دکمه ثبت‌نام
        registerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String nickname = nicknameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();
                String confirmPassword = confirmPasswordField.getText().trim();
                String gender = genderBox.getSelected();
                String question = questionBox.getSelected();
                String answer = answerField.getText().trim();
                String confirmAnswer = confirmAnswerField.getText().trim();

                if (username.isEmpty() || nickname.isEmpty() || email.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty()
                    || answer.isEmpty() || confirmAnswer.isEmpty()) {
                    errorLabel.setText("All fields are required!");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    errorLabel.setText("Passwords do not match!");
                    return;
                }
                if (!answer.equals(confirmAnswer)) {
                    errorLabel.setText("Answers do not match!");
                    return;
                }

                RegisterMenuController controller = new RegisterMenuController();
                Result result = controller.registerUser(username, nickname, email, password, confirmPassword, gender);
                if (!result.isSuccess()) {
                    errorLabel.setText(result.getMessage());
                } else {
                    // ذخیره سؤال امنیتی
                    String questionNumber = null;
                    switch (question) {
                        case "What was your first pet's name?":
                            questionNumber = "1";
                            break;
                        case "What city were you born in?":
                            questionNumber = "2";
                            break;
                        case "What is your favorite color?":
                            questionNumber = "3";
                            break;
                    }
                    if (questionNumber != null) {
                        controller.saveSecurityQuestion(questionNumber, answer);
                    }
                    // انتقال به صفحه ورود پس از موفقیت در ثبت‌نام
                    MenuNavigator.showLoginMenu();
                }
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
