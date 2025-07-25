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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.DBController;
import org.example.controllers.GameMenuController;
import org.example.models.Player;
import org.example.models.Result;

import java.util.ArrayList;
import java.util.List;

public class GameMenu implements Screen {
    private final Stage stage;
    private SpriteBatch batch;
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
        TextButton exitGameBtn = new TextButton("Exit Game", skin);

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
        table.add(exitGameBtn).colspan(2).pad(5).row();
        table.add(resultLabel).colspan(2).pad(5).width(400).row();
//        table.add(backBtn).colspan(2).pad(10).row();

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


//        loadGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Result result = controller.loadGame();
//                resultLabel.setText(result.getMessage());
//            }
//        });
        loadGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                FileHandle file = Gdx.files.local("players.json");

                // اگر فایل وجود داره و خالی نیست
                if (file.exists() && file.length() > 0) {
                    resultLabel.setText("Another player is already in a game!");
                    return;
                }

                Result result = controller.loadGame();
                resultLabel.setText(result.getMessage());
            }
        });


        deleteGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (GameMenuController.selectedPlayers.isEmpty()) {
                    resultLabel.setText("No active game to delete!");
                    return;
                }

                // Define checkBoxes first (so it's accessible inside the dialog)
                final List<CheckBox> checkBoxes = new ArrayList<>();

                // Create dialog with overridden result method
                Dialog confirmDialog = new Dialog("Delete Game", skin) {
                    @Override
                    protected void result(Object obj) {
                        boolean confirmed = (boolean) obj;

                        if (confirmed) {
                            // Check if all players confirmed
                            for (CheckBox cb : checkBoxes) {
                                if (!cb.isChecked()) {
                                    resultLabel.setText("All players must confirm deletion!");
                                    return;
                                }
                            }

                            // All confirmed → delete the game
                            Result deleteResult = controller.deleteGame();
                            if (deleteResult.isSuccess()) {
                                usernames.clear();
                                GameMenuController.selectedPlayers.clear();
                                resultLabel.setText("Game deleted successfully. No active game!");
                            } else {
                                resultLabel.setText(deleteResult.getMessage());
                            }

                        } else {
                            resultLabel.setText("Game deletion canceled!");
                        }
                    }
                };

                // Checkboxes for each player
                Table playersTable = new Table();
                for (Player player : GameMenuController.selectedPlayers) {
                    CheckBox cb = new CheckBox(" Confirm by " + player.getUsername(), skin);
                    playersTable.add(cb).left().row();
                    checkBoxes.add(cb);
                }
                confirmDialog.getContentTable().add(playersTable).pad(10);

                // Buttons
                confirmDialog.button("OK", true);
                confirmDialog.button("Cancel", false);

                confirmDialog.show(stage);
            }
        });

        exitGameBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                MenuNavigator.showMainMenu();
            }
        });


//        backBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                MenuNavigator.showMainMenu();
//            }
//        });
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); }
}
