package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.example.controllers.RegisterMenuController;
import org.example.models.Result;

import java.util.Random;

public class RegisterMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private TextField usernameField, nicknameField, emailField;
    private TextField passwordField, confirmPasswordField;
    private TextField answerField, confirmAnswerField;
    private SelectBox<String> genderBox, questionBox;
    private Label messageLabel;

    public RegisterMenu(Skin skin) {
        this.skin = skin;
        this.stage = new Stage(new FitViewport(800, 600));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Texture bg = new Texture(Gdx.files.internal("backgrounds/login_bg.png"));
        Table root = new Table();
        root.setFillParent(true);
        root.setBackground(new TextureRegionDrawable(new TextureRegion(bg)));
        stage.addActor(root);

        Label title = new Label("REGISTER", skin, "title");
        root.add(title).colspan(4).padBottom(20).row();

        messageLabel = new Label("", skin);
        messageLabel.setWrap(true);
        messageLabel.setColor(Color.RED);
        messageLabel.setFontScale(0.75f);
        messageLabel.setAlignment(Align.center);
        root.add(messageLabel).colspan(4).width(600).padBottom(20).row();
        usernameField = new TextField("", skin);
        nicknameField = new TextField("", skin);
        emailField = new TextField("", skin);
        passwordField = new TextField("", skin);
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        confirmPasswordField = new TextField("", skin);
        confirmPasswordField.setPasswordMode(true);
        confirmPasswordField.setPasswordCharacter('*');
        genderBox = new SelectBox<>(skin);
        genderBox.setItems("Male", "Female");
        questionBox = new SelectBox<>(skin);
        questionBox.setItems(
            "What was your first pet's name?",
            "What city were you born in?",
            "What is your favorite color?"
        );
        answerField = new TextField("", skin);
        confirmAnswerField = new TextField("", skin);

        root.add(new Label("Username:", skin)).pad(3);
        root.add(usernameField).width(180).height(50).pad(3);
        root.add(new Label("Nickname:", skin)).pad(3);
        root.add(nicknameField).width(180).height(50).pad(3).row();

        root.add(new Label("Email:", skin)).pad(3);
        root.add(emailField).width(180).height(50).pad(3);
        root.add(new Label("Gender:", skin)).pad(3);
        root.add(genderBox).width(180).height(50).pad(3).row();

        root.add(new Label("Password:", skin)).pad(3);
        root.add(passwordField).width(180).height(50).pad(3);
        root.add(new Label("Confirm Pass:", skin)).pad(3);
        root.add(confirmPasswordField).width(180).height(50).pad(3).row();

        TextButton randomPwdBtn = new TextButton("Generate Password", skin);
        root.add(randomPwdBtn).height(30).colspan(4).pad(5).row();

        root.add(new Label("Security Question:", skin)).pad(3);
        root.add(questionBox).colspan(3).width(400).pad(3).row();

        root.add(new Label("Answer:", skin)).pad(3);
        root.add(answerField).width(180).height(50).pad(3);
        root.add(new Label("Confirm Answer:", skin)).pad(3);
        root.add(confirmAnswerField).width(180).height(50).pad(3).row();

        TextButton registerBtn = new TextButton("Register", skin);
        TextButton backBtn = new TextButton("Back", skin);
        root.add(registerBtn).colspan(2).height(60).padTop(10);
        root.add(backBtn).colspan(2).height(60).padTop(10).row();


        randomPwdBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String generated = generateStrongPassword(10);
                passwordField.setText(generated);
                confirmPasswordField.setText(generated);
                messageLabel.setColor(Color.WHITE);
                messageLabel.setText("Generated a strong password. You can change it.");
            }
        });

        registerBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String username = usernameField.getText().trim();
                String nickname = nicknameField.getText().trim();
                String email = emailField.getText().trim();
                String password = passwordField.getText().trim();
                String confirm = confirmPasswordField.getText().trim();
                String gender = genderBox.getSelected();
                String question = questionBox.getSelected();
                String answer = answerField.getText().trim();
                String confirmAns = confirmAnswerField.getText().trim();

                if (!password.equals(confirm)) {
                    messageLabel.setColor(Color.RED);
                    messageLabel.setText("Passwords do not match!");
                    return;
                }
                if (!answer.equals(confirmAns)) {
                    messageLabel.setColor(Color.RED);
                    messageLabel.setText("Answers do not match!");
                    return;
                }

                RegisterMenuController controller = new RegisterMenuController();
                Result result = controller.registerUser(username, nickname, email, password, confirm, gender);
                if (result.isSuccess()) {
                    String questionCode = "1";
                    if (question.contains("city")) questionCode = "2";
                    else if (question.contains("color")) questionCode = "3";
                    controller.saveSecurityQuestion(questionCode, answer);
                    MenuNavigator.setSkin(skin);
                    MenuNavigator.showLoginMenu();
                } else {
                    messageLabel.setColor(Color.RED);
                    messageLabel.setText(result.getMessage());
                    System.out.println("Registration error: " + result.getMessage());
                }
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showLoginMenu();
            }
        });
    }

    private String generateStrongPassword(int length) {
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String digits = "0123456789";
        String special = "@#$%&*!?";
        String all = lower + upper + digits + special;
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(lower.charAt(rand.nextInt(lower.length())));
        sb.append(upper.charAt(rand.nextInt(upper.length())));
        sb.append(digits.charAt(rand.nextInt(digits.length())));
        sb.append(special.charAt(rand.nextInt(special.length())));
        for (int i = 4; i < length; i++) {
            sb.append(all.charAt(rand.nextInt(all.length())));
        }
        return sb.toString();
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
