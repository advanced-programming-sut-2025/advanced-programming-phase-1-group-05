package org.example.Client;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardView extends Table {
    private final Skin skin;
    private final Stage stage;
    private final Table table;
    private final List<Player> players;

    public ScoreboardView() {
        this.stage = new Stage(new ScreenViewport());
        players = new ArrayList<>();
        GameAssetManager.getInstance();
        this.skin = GameAssetManager.getSkin();
       // this.setBackground("background");
        this.pad(10);

        Label title = new Label("Scoreboard", skin, "title");
        this.add(title).colspan(2).center().padBottom(10).row();

        table = new Table();
        ScrollPane scrollPane = new ScrollPane(table, skin);
        scrollPane.setFadeScrollBars(false);
        this.add(scrollPane).colspan(2).fill().expand();
    }

    public void updatePlayer(Player player) {
        players.add(player);
    }





















}
