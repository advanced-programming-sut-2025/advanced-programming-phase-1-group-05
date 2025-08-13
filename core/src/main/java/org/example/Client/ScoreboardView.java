package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.User;
import org.example.Main;
import org.example.Server.Packets.ScoreboardUpdatePacket;
import org.example.Server.controllers.DBController;
import org.example.Server.controllers.MenuController;
import org.example.Server.models.MyGame;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreboardView implements Screen {
    private final Skin skin;
    private final Stage stage;
    private final Table contentTable = new Table();
    private List<User> users = new ArrayList<>();
    private final TextButton backButton;
    private final Label goldLabel, questsLabel, farmerSkill;
    private String sortBy = "gold";

    public ScoreboardView() {
        MyGame.setScoreboardView(this);
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        this.skin = GameAssetManager.getSkin();

        Table rootTable = new Table();
        rootTable.setFillParent(true);
        stage.addActor(rootTable);

        Label title = new Label("Scoreboard", skin, "title");
        rootTable.add(title).colspan(5).center().padBottom(20).row();

        goldLabel = new Label("Gold", skin, "subtitle");
        questsLabel = new Label("Quests", skin, "subtitle");
        farmerSkill = new Label("Farmer's Skill", skin, "subtitle");

        Label rankHeader = new Label("#", skin, "subtitle");
        Label nameHeader = new Label("Player", skin, "subtitle");

        float statColumnWidth = 120;

        rootTable.add(rankHeader).pad(5).center();
        rootTable.add(nameHeader).pad(5).left();
        rootTable.add(goldLabel).pad(5).center();
        rootTable.add(questsLabel).pad(5).center();
        rootTable.add(farmerSkill).pad(5).center();
        rootTable.row();

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        rootTable.add(scrollPane).colspan(5).expand().fill().pad(10).row();

        backButton = new TextButton("Back", skin);
        rootTable.add(backButton).colspan(5).center().padTop(15);

        setPlayers(DBController.getAllUsers());
        setUpListeners();
    }

    public void setPlayers(List<User> newUsers) {
        users.clear();
        users.addAll(newUsers);
        refreshData();
    }

    public void updateUser(ScoreboardUpdatePacket packet) {
        for (User user : users) {
            if (user.getUsername().equals(packet.username)) {
                user.updateUserInfo(packet.gold, "gold");
                user.updateUserInfo(packet.quests, "quests");
                user.updateUserInfo(packet.skill, "skill");

                break;
            }
        }
        DBController.saveAllUsers();
        refreshData();
    }


    public void refreshData() {
        contentTable.clear();
        contentTable.top();

        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparingInt(u -> -(int) u.getInfo(sortBy)));
        String currentUsername = MyGame.getCurrentPlayer().getUsername();

        Color[] podiumColors = {Color.RED, Color.GREEN, Color.YELLOW};

        for (int i = 0; i < sortedUsers.size(); i++) {
            User user = sortedUsers.get(i);

            Label rankLabel = new Label(String.valueOf(i + 1), skin);
            Label nameLabel = new Label(user.getUsername(), skin);
            Label goldVal = new Label(String.valueOf(user.getInfo("gold")), skin);
            Label questsVal = new Label(String.valueOf(user.getInfo("quests")), skin);
            Label skillVal = new Label(String.valueOf(user.getInfo("skill")), skin);

            if (user.getUsername().equals(currentUsername)) {
                nameLabel.setFontScale(1.1f);
                nameLabel.setColor(Color.WHITE);
            } else {
                nameLabel.setFontScale(1f);
                nameLabel.setColor(Color.WHITE);
            }

            if (i < 3) {
                rankLabel.setColor(podiumColors[i]);
                nameLabel.setColor(podiumColors[i]);
            } else {
                rankLabel.setColor(Color.WHITE);
            }

            contentTable.top().left();
            contentTable.add(rankLabel).pad(5, 55, 5, 0).left();
            contentTable.add(nameLabel).pad(5, 75, 5, 0).left();
            contentTable.add(goldVal).pad(5, 250, 5, 0).center();
            contentTable.add(questsVal).pad(5, 270, 5, 0).center();
            contentTable.add(skillVal).pad(5, 300, 5, 0).center();
            contentTable.row();

        }
    }

    public void setUpListeners() {
        goldLabel.setTouchable(Touchable.enabled);
        questsLabel.setTouchable(Touchable.enabled);
        farmerSkill.setTouchable(Touchable.enabled);

        goldLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sortBy = "gold";
                refreshData();
            }
        });

        questsLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sortBy = "quests";
                refreshData();
            }
        });

        farmerSkill.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sortBy = "skill";
                refreshData();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                MyGame.getCurrentPlayer().addGold(10);
                //Main.getMain().setScreen(new MainMenu(GameAssetManager.getSkin()));
            }
        });
    }

    @Override
    public void show() {}

    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        goldLabel.setColor(Color.WHITE);
        questsLabel.setColor(Color.WHITE);
        farmerSkill.setColor(Color.WHITE);

        switch (sortBy) {
            case "gold":   goldLabel.setColor(Color.GOLD); break;
            case "quests": questsLabel.setColor(Color.GOLD); break;
            case "skill":  farmerSkill.setColor(Color.GOLD); break;
        }

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();
    }
    public List<User> getUsers() {
        return users;
    }
}
