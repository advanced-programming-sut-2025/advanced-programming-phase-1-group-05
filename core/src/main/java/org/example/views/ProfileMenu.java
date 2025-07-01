package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.ProfileMenuController;
import org.example.controllers.RegisterMenuController;
import org.example.models.Result;

public class ProfileMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final ProfileMenuController controller;

    private TextField usernameField, nicknameField, emailField, oldPasswordField, newPasswordField;
    private Label genderLabel, infoLabel, resultLabel;

    public ProfileMenu(Skin skin, ProfileMenuController controller) {
        this.skin = skin;
        this.controller = controller;
        this.stage = new Stage(new ScreenViewport());
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // Root Table
        Table root = new Table();
        root.top().padTop(30).padLeft(50).padRight(50);
        root.defaults().pad(8);
        root.setFillParent(true);

        // Fields
        usernameField = new TextField("", skin);
        nicknameField = new TextField("", skin);
        emailField = new TextField("", skin);
        oldPasswordField = new TextField("", skin);
        newPasswordField = new TextField("", skin);

        oldPasswordField.setPasswordMode(true);
        oldPasswordField.setPasswordCharacter('*');
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        // Labels
        genderLabel = new Label("Gender: " + RegisterMenuController.currentUser.getGender(), skin);
        infoLabel = new Label("", skin);
        infoLabel.setWrap(true);
        resultLabel = new Label("", skin);
        resultLabel.setColor(Color.RED);

        // Buttons
        TextButton updateUsernameBtn = new TextButton("Update Username", skin);
        TextButton updateNicknameBtn = new TextButton("Update Nickname", skin);
        TextButton updateEmailBtn = new TextButton("Update Email", skin);
        TextButton updatePasswordBtn = new TextButton("Update Password", skin);
        TextButton backBtn = new TextButton("Back", skin);

        // Build UI
        root.add(new Label("Profile Menu", skin, "title")).colspan(2).padBottom(20).row();

        root.add(new Label("Username:", skin));

        root.add(usernameField).width(250).row();
        root.add(updateUsernameBtn).colspan(2).row();

        root.add(new Label("Nickname",skin));
        root.add(nicknameField).width(250).row();
        root.add(updateNicknameBtn).colspan(2).row();

        root.add(new Label("Email",skin));
        root.add(emailField).width(250).row();
        root.add(updateEmailBtn).colspan(2).row();

        root.add(new Label("OldPass",skin));
        root.add(oldPasswordField).width(250).row();
        root.add(new Label("NewPass",skin));
        root.add(newPasswordField).width(250).row();
        root.add(updatePasswordBtn).colspan(2).row();

        root.add(genderLabel).colspan(2).row();
        root.add(infoLabel).colspan(2).width(500).row();
        root.add(resultLabel).colspan(2).row();
        root.add(backBtn).colspan(2).padTop(15).row();

        // Add scroll if needed
        ScrollPane scrollPane = new ScrollPane(root, skin);
        scrollPane.setFillParent(true);

        Table container = new Table();
        container.setFillParent(true);
        container.add(scrollPane).expand().fill();
        stage.addActor(container);

        // Actions
        attachListeners(updateUsernameBtn, updateNicknameBtn, updateEmailBtn, updatePasswordBtn, backBtn);
        showUserInfo();
    }

    private void attachListeners(TextButton updateUsernameBtn, TextButton updateNicknameBtn,
                                 TextButton updateEmailBtn, TextButton updatePasswordBtn, TextButton backBtn) {

        updateUsernameBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showResult(controller.handleProfileCommand("change username -u " + usernameField.getText()));
            }
        });

        updateNicknameBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showResult(controller.handleProfileCommand("change nickname -u " + nicknameField.getText()));
            }
        });

        updateEmailBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                showResult(controller.handleProfileCommand("change email -e " + emailField.getText()));
            }
        });

        updatePasswordBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String cmd = "change password -p " + newPasswordField.getText() + " -o " + oldPasswordField.getText();
                showResult(controller.handleProfileCommand(cmd));
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showMainMenu();
            }
        });
    }

    private void showUserInfo() {
        Result info = controller.handleProfileCommand("user info");
        if (info.isSuccess()) {
            infoLabel.setText(info.getMessage());
        }
    }

    private void showResult(Result result) {
        resultLabel.setText(result.getMessage());
        resultLabel.setColor(result.isSuccess() ? Color.GREEN : Color.RED);
        if (result.isSuccess()) showUserInfo();
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f); // پس زمینه خاکستری تیره
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
