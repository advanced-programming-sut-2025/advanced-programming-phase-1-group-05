package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.DBController;
import org.example.controllers.GameMenuController;
import org.example.models.Player;
import org.example.models.Result;
import org.example.models.User;
import org.example.models.UserDatabase;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.example.views.GameScreen.TILE_SIZE;

public class MapSelectionScreen implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final GameMenuController controller;
    private final List<String> usernames;
    private final Map<String, SelectBox<String>> playerMapSelections = new HashMap<>();
    private final Label resultLabel;

    public MapSelectionScreen(List<String> usernames, Skin skin, GameMenuController controller) {
        this.usernames = usernames;
        this.skin = skin;
        this.controller = controller;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Select Map for Each Player", skin, "title");
        table.add(titleLabel).colspan(2).padBottom(15).row();

        for (String username : usernames) {
            table.add(new Label(username + "'s Map:", skin)).pad(5);
            SelectBox<String> mapBox = new SelectBox<>(skin);
            mapBox.setItems("Map1", "Map2", "Map3", "Map4");
            playerMapSelections.put(username, mapBox);
            table.add(mapBox).width(200).pad(5).row();
        }

        TextButton confirmBtn = new TextButton("Start Game", skin);
        resultLabel = new Label("", skin);
        resultLabel.setWrap(true);

        table.add(confirmBtn).colspan(2).pad(10).row();
        table.add(resultLabel).colspan(2).width(400).pad(5).row();

        confirmBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Map<String, String> selections = new HashMap<>();
                for (String username : usernames) {
                    String selectedMap = playerMapSelections.get(username).getSelected();
                    selections.put(username, selectedMap);
                }
                Result result = controller.submitAllMapSelections(selections);
                if (!result.isSuccess()) {
                    resultLabel.setText(result.getMessage());
                } else {
                    ArrayList<Player> players = new ArrayList<>();
                    for (String username : usernames) {
                        User user = UserDatabase.getUserByUsername(username);
                        Player player = new Player(user);
                        players.add(player);
                        switch (selections.get(username).toLowerCase()) {
                            case "map1" : {
                                player.setFarm(new Rectangle(10f * TILE_SIZE, 10f * TILE_SIZE, 50f * TILE_SIZE, 50f * TILE_SIZE));
                                System.out.println("set " + username + "'s farm");
                                break;
                            }
                            case "map2" : {
                                player.setFarm(new Rectangle(80 * TILE_SIZE, 10 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE));
                                System.out.println("set " + username + "'s farm");
                                break;
                            }
                            case "map3" : {
                                player.setFarm(new Rectangle(10 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE));
                                System.out.println("set " + username + "'s farm");
                                break;
                            }
                            case "map4" : {
                                player.setFarm(new Rectangle(80 * TILE_SIZE, 80 * TILE_SIZE, 50 * TILE_SIZE, 50 * TILE_SIZE));
                                System.out.println("set " + username + "'s farm");
                                break;
                            }
                        }
                    }
                    DBController.saveGameState();
                    resultLabel.setText("Game started!");
                    //MenuNavigator.showGameScreen();
                    MenuNavigator.showGameScreen(players);
                }
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
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
