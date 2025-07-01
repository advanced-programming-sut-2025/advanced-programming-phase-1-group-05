//
//package org.example.views;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import org.example.controllers.GameMenuController;
//import org.example.models.Result;
//
//public class GameMenu implements Screen {
//    private final Stage stage;
//    private final Skin skin;
//    private final GameMenuController controller;
//    private final TextField playerNameField;
//    private final Label resultLabel;
//
//    public GameMenu(Skin skin, GameMenuController controller) {
//        this.skin = skin;
//        this.controller = controller;
//        this.stage = new Stage(new ScreenViewport());
//        Gdx.input.setInputProcessor(stage);
//
//        Table table = new Table();
//        table.setFillParent(true);
//        stage.addActor(table);
//
//        Label titleLabel = new Label("Game Menu", skin, "title");
//        playerNameField = new TextField("", skin);
//        resultLabel = new Label("", skin);
//        resultLabel.setWrap(true);
//
//        TextButton addPlayerBtn = new TextButton("Add Player", skin);
//        TextButton startGameBtn = new TextButton("Start Game", skin);
//        TextButton loadGameBtn = new TextButton("Load Game", skin);
//        TextButton deleteGameBtn = new TextButton("Delete Game", skin);
//        TextButton backBtn = new TextButton("Back to Main Menu", skin);
//
//        table.add(titleLabel).colspan(2).padBottom(15).row();
//        table.add(new Label("Player Name:", skin)).pad(5);
//        table.add(playerNameField).width(200).pad(5).row();
//        table.add(addPlayerBtn).colspan(2).pad(5).row();
//        table.add(startGameBtn).colspan(2).pad(5).row();
//        table.add(loadGameBtn).colspan(2).pad(5).row();
//        table.add(deleteGameBtn).colspan(2).pad(5).row();
//        table.add(resultLabel).colspan(2).pad(5).width(400).row();
//        table.add(backBtn).colspan(2).pad(10).row();
//
//        addPlayerBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                String username = playerNameField.getText();
//                Result result = controller.newGame("game new -u " + username);
//                resultLabel.setText(result.getMessage());
//            }
//        });
//
//        startGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Result result = controller.startGameIfReady();
//                resultLabel.setText(result.getMessage());
//            }
//        });
//
//        loadGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Result result = controller.loadGame();
//                resultLabel.setText(result.getMessage());
//            }
//        });
//
//        deleteGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Result result = controller.deleteGame();
//                resultLabel.setText(result.getMessage());
//            }
//        });
//
//        backBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                MenuNavigator.showMainMenu();
//            }
//        });
//    }
//
//    @Override
//    public void show() {
//        Gdx.input.setInputProcessor(stage);
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        stage.act();
//        stage.draw();
//    }
//
//    @Override public void resize(int width, int height) {}
//    @Override public void pause() {}
//    @Override public void resume() {}
//    @Override public void hide() {}
//    @Override public void dispose() { stage.dispose(); }
//}
package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.controllers.GameMenuController;
import org.example.models.Player;
import org.example.models.Result;

import java.util.ArrayList;
import java.util.List;

public class GameMenu implements Screen {
    private final Stage stage;
    private final Skin skin;
    private final GameMenuController controller;
    private final List<TextField> playerFields = new ArrayList<>();
    private final Label resultLabel;
    List<String> usernames = new ArrayList<>();

    public GameMenu(Skin skin, GameMenuController controller) {
        this.skin = skin;
        this.controller = controller;
        this.stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Game Menu", skin, "title");
        resultLabel = new Label("", skin);
        resultLabel.setWrap(true);

        TextButton addPlayersBtn = new TextButton("Add Players", skin);
        TextButton startGameBtn = new TextButton("Start Game", skin);
        TextButton loadGameBtn = new TextButton("Load Game", skin);
        TextButton deleteGameBtn = new TextButton("Delete Game", skin);
        TextButton backBtn = new TextButton("Back to Main Menu", skin);

        table.add(titleLabel).colspan(2).padBottom(15).row();

        // 3 input fields for players
        for (int i = 1; i <= 3; i++) {
            TextField playerField = new TextField("", skin);
            playerFields.add(playerField);
            table.add(new Label("Player " + i + ":", skin)).pad(5);
            table.add(playerField).width(200).pad(5).row();
        }

        table.add(addPlayersBtn).colspan(2).pad(5).row();
        table.add(startGameBtn).colspan(2).pad(5).row();
        table.add(loadGameBtn).colspan(2).pad(5).row();
        table.add(deleteGameBtn).colspan(2).pad(5).row();
        table.add(resultLabel).colspan(2).pad(5).width(400).row();
        table.add(backBtn).colspan(2).pad(10).row();

        addPlayersBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                resultLabel.setText("");

//                List<String> usernames = new ArrayList<>();
                for (TextField field : playerFields) {
                    String username = field.getText().trim();
                    if (username.isEmpty()) {
                        resultLabel.setText("Please enter all 3 player names.");
                        return;
                    }
                    usernames.add(username);
                }

                // ساختن دستور به صورت game new -u user1 -u user2 -u user3
                StringBuilder command = new StringBuilder("game new");
                for (String username : usernames) {
                    command.append(" -u ").append(username);
                }

                Result result = controller.newGame(command.toString());
                resultLabel.setText(result.getMessage());
            }
        });

        startGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Result result = controller.startGameIfReady();
                resultLabel.setText(result.getMessage());

                if (result.isSuccess()) {
                    // استخراج نام بازیکنان برای نمایش صفحه انتخاب مپ
                    List<String> usernames = new ArrayList<>();
                    for (Player player : GameMenuController.selectedPlayers) {
                        usernames.add(player.getUsername());
                    }
                    MenuNavigator.showMapSelectionScreen(usernames, skin, controller);
                }
                if (usernames == null) {
                    resultLabel.setText("Please enter all 3 player names.");
                    MenuNavigator.showGameMenu();
                }
                for (String username : usernames) {
                    if (username.isEmpty() || username == null) {
                        resultLabel.setText("Please enter all 3 player names.");
                        return;
                    }
                }
            }
        });


        loadGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Result result = controller.loadGame();
                resultLabel.setText(result.getMessage());
            }
        });

        deleteGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Result result = controller.deleteGame();
                resultLabel.setText(result.getMessage());
            }
        });

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showMainMenu();
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
