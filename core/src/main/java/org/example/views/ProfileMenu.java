package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.ProfileMenuController;
import org.example.controllers.RegisterMenuController;
import org.example.models.Result;
import org.example.models.User;

public class ProfileMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private ProfileMenuController controller;
    private Texture avatarTexture;

    // UI components
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
        stage.clear();

        User currentUser = RegisterMenuController.currentUser;
        if (currentUser == null) {
            MenuNavigator.showLoginMenu();
            return;
        }

        controller = new ProfileMenuController(currentUser);

        Table table = new Table(skin);
        table.setFillParent(true);
        table.center();
        stage.addActor(table);
//        String avatarPath = "assets/NPCs/sebastian/avatar.png";
//        if (currentUser.getGender().equals("Male")) {
//            avatarPath = "assets/NPCs/sebastian/avatar.png";
//        } else {
//            avatarPath = "assets/NPCs/sebastian/avatar.png";
//        }
        String path = "NPCs/sebastian/avatar.png";
        if (Gdx.files.internal(path).exists()) {
            avatarTexture = new Texture(Gdx.files.internal(path));
        } else {
            avatarTexture = new Texture(Gdx.files.internal("default.png"));
        }


        avatarTexture = new Texture(Gdx.files.internal(path));
        Image avatarImage = new Image(avatarTexture);


        usernameField = new TextField("", skin);
        nicknameField = new TextField("", skin);
        emailField = new TextField("", skin);
        oldPasswordField = new TextField("", skin);
        newPasswordField = new TextField("", skin);

        oldPasswordField.setPasswordMode(true);
        oldPasswordField.setPasswordCharacter('*');
        newPasswordField.setPasswordMode(true);
        newPasswordField.setPasswordCharacter('*');

        genderLabel = new Label("Gender: " + currentUser.getGender(), skin);
        infoLabel = new Label("", skin);
        resultLabel = new Label("", skin);
        resultLabel.setColor(Color.RED);
        infoLabel.setWrap(true);
        infoLabel.setAlignment(Align.center);
        infoLabel.setWidth(400);

        TextButton updateUsernameBtn = new TextButton("Update Username", skin);
        TextButton updateNicknameBtn = new TextButton("Update Nickname", skin);
        TextButton updateEmailBtn = new TextButton("Update Email", skin);
        TextButton updatePasswordBtn = new TextButton("Update Password", skin);
        TextButton backBtn = new TextButton("Back", skin);

        avatarImage.setName("avatarImage");
        table.add(avatarImage).size(80, 80).colspan(2).padBottom(10).row();
        table.add(new Label("Profile Menu", skin, "title")).colspan(2).padBottom(15).row();

        table.add(new Label("Username:", skin)).pad(5);
        table.add(usernameField).width(250).pad(5).row();
        table.add(updateUsernameBtn).colspan(2).pad(5).row();

        table.add(new Label("Nickname:", skin)).pad(5);
        table.add(nicknameField).width(250).pad(5).row();
        table.add(updateNicknameBtn).colspan(2).pad(5).row();

        table.add(new Label("Email:", skin)).pad(5);
        table.add(emailField).width(250).pad(5).row();
        table.add(updateEmailBtn).colspan(2).pad(5).row();

        table.add(new Label("Old Password:", skin)).pad(5);
        table.add(oldPasswordField).width(250).pad(5).row();
        table.add(new Label("New Password:", skin)).pad(5);
        table.add(newPasswordField).width(250).pad(5).row();
        table.add(updatePasswordBtn).colspan(2).pad(5).row();

        table.add(genderLabel).colspan(2).pad(5).row();
        table.add(infoLabel).colspan(2).width(400).expandX().fillX().pad(5).row();
        table.add(resultLabel).colspan(2).pad(5).row();
        table.add(backBtn).colspan(2).padTop(10).row();

        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFillParent(true);
        scrollPane.setScrollingDisabled(true, false);

        stage.addActor(scrollPane);
        showUserInfo();

        // Listeners
        updateUsernameBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String cmd = "change username -u " + usernameField.getText();
                showResult(controller.handleProfileCommand(cmd));
            }
        });

        updateNicknameBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String cmd = "change nickname -u " + nicknameField.getText();
                showResult(controller.handleProfileCommand(cmd));
            }
        });

        updateEmailBtn.addListener(new ChangeListener() {
            @Override public void changed(ChangeEvent event, Actor actor) {
                String cmd = "change email -e " + emailField.getText();
                showResult(controller.handleProfileCommand(cmd));
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
        if (result.isSuccess()) {
            showUserInfo();
        }
    }

    @Override public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
