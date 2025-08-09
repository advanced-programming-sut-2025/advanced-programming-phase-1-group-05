package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.User;
import org.example.Server.controllers.DBController;
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
    private final Map<User, Label[]> labelMap = new HashMap<>();
    private final TextButton backButton;
    private final Label goldLabel, questsLabel, farmerSkill;
    private String sortBy = "gold";

    public ScoreboardView() {
        MyGame.setScoreboardView(this);
        this.stage = new Stage(new ScreenViewport());
        GameAssetManager.getInstance();
        Gdx.input.setInputProcessor(stage);

        GameAssetManager.getInstance();
        this.skin = GameAssetManager.getSkin();

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        Label title = new Label("Scoreboard", skin, "title");
        mainTable.add(title).colspan(5).center().padBottom(20).row();

        goldLabel = new Label("Gold", skin, "subtitle");
        questsLabel = new Label("Quests", skin, "subtitle");
        farmerSkill = new Label("Farmer's Skill", skin, "subtitle");
        mainTable.add(new Label("#", skin, "subtitle")).pad(5);
        mainTable.add(new Label("Player", skin, "subtitle")).pad(5);
        mainTable.add(goldLabel).pad(5);
        mainTable.add(questsLabel).pad(5);
        mainTable.add(farmerSkill).pad(5).row();
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setFadeScrollBars(false);

        mainTable.add(scrollPane).colspan(5).expand().fill().pad(10);
        backButton = new TextButton("Back", skin);
        mainTable.add(backButton).pad(10);
        setPlayers( DBController.getAllUsers());
        setUpListeners();
    }

    public void setPlayers(List<User> newUsers) {
        users.clear();
        users.addAll(newUsers);
        buildRows();
    }

    private void buildRows() {
        contentTable.clear();
        labelMap.clear();

        for (User user : users) {
            Label rank = new Label("-", skin);
            Label name = new Label(user.getUsername(), skin);
            Label gold = new Label("0", skin);
            Label quests = new Label("0", skin);
            Label skill = new Label("0", skin);

            contentTable.add(rank).padRight(20);
            contentTable.add(name).pad(20);
            contentTable.add(gold).pad(20);
            contentTable.add(quests).pad(20);
            contentTable.add(skill).pad(20).row();

            labelMap.put(user, new Label[]{rank, name, gold, quests, skill});
        }
    }

    private void refreshData() {
        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparingInt(u -> -(int) u.getInfo(sortBy)));
        String currentUsername = MyGame.getCurrentPlayer().getUsername();

        for (int i = 0; i < sortedUsers.size(); i++) {
            User user = sortedUsers.get(i);
            Label[] labels = labelMap.get(user);

            if (labels == null) continue;

            labels[0].setText(String.valueOf(i + 1));

            labels[1].setText(user.getUsername());

            if (user.getUsername().equals(currentUsername)) {
                labels[1].setFontScale(1.1f);
                labels[1].setColor(Color.WHITE);
            } else {
                labels[1].setFontScale(1f);
                labels[1].setColor(Color.WHITE);
            }

            Color[] podiumColors = {Color.GOLD, Color.LIGHT_GRAY, Color.BROWN};
            if (i < 3) {
                labels[0].setColor(podiumColors[i]);
                labels[1].setColor(podiumColors[i]);
            } else {
                labels[0].setColor(Color.WHITE);
            }

            labels[2].setText(String.valueOf(user.getInfo("gold")));
            labels[3].setText(String.valueOf(user.getInfo("quests")));
            labels[4].setText(String.valueOf(user.getInfo("skill")));
        }
    }

    public void setUpListeners() {
        goldLabel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                sortBy = "gold";
            }
        });
        questsLabel.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                sortBy = "quests";
            }
        });
        farmerSkill.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                sortBy = "skill";
            }
        });
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent changeEvent, Actor actor) {
                MyGame.getCurrentPlayer().addGold(10);
            }
        });
    }

    @Override
    public void show() {}

    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        refreshData();
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
}

