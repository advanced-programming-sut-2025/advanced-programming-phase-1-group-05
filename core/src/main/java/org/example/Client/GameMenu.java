package org.example.Client;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Screen;
//import com.badlogic.gdx.files.FileHandle;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.*;
//import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import org.example.Server.controllers.GameMenuController;
//import org.example.Common.Player;
//import org.example.Server.models.Result;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameMenu implements Screen {
//    private final Stage stage;
//    private SpriteBatch batch;
//    private final Skin skin;
//    private final GameMenuController controller;
//    private final List<TextField> playerFields = new ArrayList<>();
//    private final Label resultLabel;
//    List<String> usernames = new ArrayList<>();
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
//        resultLabel = new Label("", skin);
//        resultLabel.setWrap(true);
//
//        TextButton addPlayersBtn = new TextButton("Add Players", skin);
//        TextButton startGameBtn = new TextButton("Start Game", skin);
//        TextButton loadGameBtn = new TextButton("Load Game", skin);
//        TextButton deleteGameBtn = new TextButton("Delete Game", skin);
//        TextButton backBtn = new TextButton("Back to Main Menu", skin);
//        TextButton exitGameBtn = new TextButton("Exit Game", skin);
//
//        table.add(titleLabel).colspan(2).padBottom(15).row();
//
//        // 3 input fields for players
//        for (int i = 1; i <= 3; i++) {
//            TextField playerField = new TextField("", skin);
//            playerFields.add(playerField);
//            table.add(new Label("Player " + i + ":", skin)).pad(5);
//            table.add(playerField).width(200).pad(5).row();
//        }
//
//        table.add(addPlayersBtn).colspan(2).pad(5).row();
//        table.add(startGameBtn).colspan(2).pad(5).row();
//        table.add(loadGameBtn).colspan(2).pad(5).row();
//        table.add(deleteGameBtn).colspan(2).pad(5).row();
//        table.add(exitGameBtn).colspan(2).pad(5).row();
//        table.add(resultLabel).colspan(2).pad(5).width(400).row();
//
//        addPlayersBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                resultLabel.setText("");
//
////                List<String> usernames = new ArrayList<>();
//                for (TextField field : playerFields) {
//                    String username = field.getText().trim();
//                    if (username.isEmpty()) {
//                        resultLabel.setText("Please enter all 3 player names.");
//                        return;
//                    }
//                    usernames.add(username);
//                }
//
//                StringBuilder command = new StringBuilder("game new");
//                for (String username : usernames) {
//                    command.append(" -u ").append(username);
//                }
//
//                Result result = controller.newGame(command.toString());
//                resultLabel.setText(result.getMessage());
//            }
//        });
//
//        startGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                Result result = controller.startGameIfReady();
//                resultLabel.setText(result.getMessage());
//
//                if (result.isSuccess()) {
//                    List<String> usernames = new ArrayList<>();
//                    for (Player player : GameMenuController.selectedPlayers) {
//                        usernames.add(player.getUsername());
//                    }
//                    MenuNavigator.showMapSelectionScreen(usernames, skin, controller);
//                }
//                if (usernames == null) {
//                    resultLabel.setText("Please enter all 3 player names.");
//                    MenuNavigator.showGameMenu();
//                }
//                for (String username : usernames) {
//                    if (username.isEmpty() || username == null) {
//                        resultLabel.setText("Please enter all 3 player names.");
//                        return;
//                    }
//                }
//            }
//        });
//
//        loadGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                FileHandle file = Gdx.files.local("players.json");
//
//                // اگر فایل وجود داره و خالی نیست
//                if (file.exists() && file.length() > 0) {
//                    resultLabel.setText("Another player is already in a game!");
//                    return;
//                }
//
//                Result result = controller.loadGame();
//                resultLabel.setText(result.getMessage());
//            }
//        });
//
//
//        deleteGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                if (GameMenuController.selectedPlayers.isEmpty()) {
//                    resultLabel.setText("No active game to delete!");
//                    return;
//                }
//
//                // Define checkBoxes first (so it's accessible inside the dialog)
//                final List<CheckBox> checkBoxes = new ArrayList<>();
//
//                // Create dialog with overridden result method
//                Dialog confirmDialog = new Dialog("Delete Game", skin) {
//                    @Override
//                    protected void result(Object obj) {
//                        boolean confirmed = (boolean) obj;
//
//                        if (confirmed) {
//                            // Check if all players confirmed
//                            for (CheckBox cb : checkBoxes) {
//                                if (!cb.isChecked()) {
//                                    resultLabel.setText("All players must confirm deletion!");
//                                    return;
//                                }
//                            }
//
//                            // All confirmed → delete the game
//                            Result deleteResult = controller.deleteGame();
//                            if (deleteResult.isSuccess()) {
//                                usernames.clear();
//                                GameMenuController.selectedPlayers.clear();
//                                resultLabel.setText("Game deleted successfully. No active game!");
//                            } else {
//                                resultLabel.setText(deleteResult.getMessage());
//                            }
//
//                        } else {
//                            resultLabel.setText("Game deletion canceled!");
//                        }
//                    }
//                };
//
//                // Checkboxes for each player
//                Table playersTable = new Table();
//                for (Player player : GameMenuController.selectedPlayers) {
//                    CheckBox cb = new CheckBox(" Confirm by " + player.getUsername(), skin);
//                    playersTable.add(cb).left().row();
//                    checkBoxes.add(cb);
//                }
//                confirmDialog.getContentTable().add(playersTable).pad(10);
//
//                // Buttons
//                confirmDialog.button("OK", true);
//                confirmDialog.button("Cancel", false);
//
//                confirmDialog.show(stage);
//            }
//        });
//
//        exitGameBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                MenuNavigator.showMainMenu();
//            }
//        });
//    }
//
//
//    @Override
//    public void show() {
//        Gdx.input.setInputProcessor(stage);
//        batch = new SpriteBatch();
//    }
//
//    @Override
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        batch.begin();
//        batch.end();
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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.example.Common.Lobby;
import org.example.Common.Player;
import org.example.Common.Network.ResultResponse;
import org.example.Main;
import org.example.Server.controllers.GameMenuController;

import java.util.ArrayList;
import java.util.List;

public class GameMenu implements Screen {
    private final Stage stage;
    private SpriteBatch batch;
    private final Skin skin;
    private final GameMenuController controller;
    private final List<TextField> playerFields = new ArrayList<>();
    private final Label resultLabel;
    private final List<String> usernames = new ArrayList<>();

    // لابی
    private final List<Lobby> currentLobbies = new ArrayList<>();
    private com.badlogic.gdx.scenes.scene2d.ui.List<Lobby> lobbyList;
    private Lobby selectedLobby;
    private Label lobbyDetail;
    private TextField joinIdField;

    public GameMenu(Skin skin, GameMenuController controller) {
        this.skin = skin;
        this.controller = controller;
        Viewport viewport = new FitViewport(800, 480);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Game Menu", skin, "title");
        resultLabel = new Label("", skin);
        resultLabel.setWrap(true);

        // دکمه‌های قبلی
        TextButton addPlayersBtn = new TextButton("Add Players", skin);
        TextButton startGameBtn = new TextButton("Start Game", skin);
        TextButton loadGameBtn = new TextButton("Load Game", skin);
        TextButton deleteGameBtn = new TextButton("Delete Game", skin);
        TextButton exitGameBtn = new TextButton("Exit Game", skin);

        // دکمه‌های لابی
        TextButton refreshLobbyBtn = new TextButton("Refresh Lobbies", skin);
        TextButton createLobbyBtn = new TextButton("Create Lobby", skin);
        TextButton joinLobbyBtn = new TextButton("Join Lobby", skin);
        TextButton leaveLobbyBtn = new TextButton("Leave Lobby", skin);

        table.add(titleLabel).colspan(2).padBottom(15).row();

        // سه ورودی بازیکنان
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

        // ----- لیست لابی‌ها -----
        lobbyList = new com.badlogic.gdx.scenes.scene2d.ui.List<>(skin);
        ScrollPane scrollPane = new ScrollPane(lobbyList, skin);
        lobbyDetail = new Label("", skin);

        table.add(new Label("Lobbies:", skin)).colspan(4).padTop(10).row();
        table.add(scrollPane).width(300).height(150).colspan(4).pad(5).row();

        table.add(refreshLobbyBtn).pad(5);
        table.add(createLobbyBtn).pad(5);
        table.add(joinLobbyBtn).pad(5);
        table.add(leaveLobbyBtn).pad(5).row();

        joinIdField = new TextField("", skin);
        joinIdField.setMessageText("Lobby ID");
        TextButton joinByIdBtn = new TextButton("Join by ID", skin);

        table.add(joinIdField).width(150).pad(5);
        table.add(joinByIdBtn).pad(5).colspan(3).left().row();

        table.add(lobbyDetail).colspan(4).pad(10).row();

        table.add(exitGameBtn).colspan(2).pad(5).row();
        table.add(resultLabel).colspan(2).pad(5).width(400).row();

        // --------- Events ----------
        refreshLobbyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                refreshLobbies();
            }
        });

        createLobbyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showCreateLobbyDialog();
            }
        });

        joinLobbyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                selectedLobby = lobbyList.getSelected();
                if (selectedLobby == null) return;

                if (selectedLobby.isPrivate()) {
                    showPasswordDialog(selectedLobby);
                } else {
                    boolean ok = controller.joinLobby(selectedLobby.getId(), new Player(GameMenuController.currentUser), null);
                    if (ok) {
                        updateLobbyDetail();
                    } else {
                        resultLabel.setText("⚠️ Lobby is full (max 4 players) or password is wrong!");
                    }
                }
            }
        });

        joinByIdBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String id = joinIdField.getText().trim();
                if (id.isEmpty()) {
                    resultLabel.setText("Please enter a lobby ID.");
                    return;
                }

                Lobby target = null;
                for (Lobby lobby : currentLobbies) {
                    if (lobby.getId().equalsIgnoreCase(id)) {
                        target = lobby;
                        break;
                    }
                }

                if (target == null) {
                    // اگر لابی visible نیست ولی ID درست باشه، سرور تشخیص میده
                    // فرض می‌کنیم ممکنه private هم باشه و رمز بخواد
                    selectedLobby = new Lobby("Unknown", false, null, false, new Player(Main.currentUser));
                    selectedLobby = new Lobby(id, false, null, false, new Player(Main.currentUser)); // فقط ID می‌خوایم
                    showPasswordDialog(selectedLobby);
                } else {
                    selectedLobby = target;
                    if (selectedLobby.isPrivate()) {
                        showPasswordDialog(selectedLobby);
                    } else {
                        boolean ok = controller.joinLobby(selectedLobby.getId(), new Player(Main.currentUser), null);
                        if (ok) {
                            updateLobbyDetail();
                        } else {
                            resultLabel.setText("⚠️ Could not join lobby!");
                        }
                    }
                }
            }
        });


        leaveLobbyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.leaveLobby(new Player(GameMenuController.currentUser));
                selectedLobby = null;
                refreshLobbies();
                lobbyDetail.setText("");
            }
        });

        refreshLobbies();
    }

    private void refreshLobbies() {
        currentLobbies.clear();
        currentLobbies.addAll(controller.getActiveLobbies());
        lobbyList.setItems(currentLobbies.toArray(new Lobby[0]));
    }

    private void updateLobbyDetail() {
        Lobby lobby = controller.getCurrentLobby();
        if (lobby != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Lobby: ").append(lobby.getName()).append("\nPlayers: ");
            lobby.getPlayers().forEach(p -> sb.append(p.getUsername()).append(" "));
            lobbyDetail.setText(sb.toString());
        }
    }

    private void showCreateLobbyDialog() {
        Dialog dialog = new Dialog("Create Lobby", skin) {
            @Override
            protected void result(Object obj) {
                if ((boolean) obj) {
                    controller.createLobby(
                        nameField.getText(),
                        privateBox.isChecked(),
                        privateBox.isChecked() ? passwordField.getText() : null,
                        visibleBox.isChecked(),
                        new Player(Main.currentUser)
                    );
                    refreshLobbies();
                }
            }

            TextField nameField = new TextField("", skin);
            CheckBox privateBox = new CheckBox("Private", skin);
            TextField passwordField = new TextField("", skin);
            CheckBox visibleBox = new CheckBox("Visible (show in list)", skin);

            {
                passwordField.setPasswordMode(true);
                passwordField.setDisabled(true);

                privateBox.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        passwordField.setDisabled(!privateBox.isChecked());
                    }
                });

                visibleBox.setChecked(true);

                getContentTable().add(new Label("Name:", skin)).pad(5);
                getContentTable().add(nameField).pad(5).row();
                getContentTable().add(privateBox).colspan(2).pad(5).row();
                getContentTable().add(new Label("Password:", skin)).pad(5);
                getContentTable().add(passwordField).pad(5).row();
                getContentTable().add(visibleBox).colspan(2).pad(5).row();

                button("Create", true);
                button("Cancel", false);
            }
        };
        dialog.show(stage);
    }

    private void showPasswordDialog(Lobby lobby) {
        Dialog dialog = new Dialog("Enter Password", skin) {
            @Override
            protected void result(Object obj) {
                if ((boolean) obj) {
                    boolean ok = controller.joinLobby(lobby.getId(),
                        new Player(Main.currentUser),
                        passwordField.getText()
                    );
                    if (ok) updateLobbyDetail();
                }
            }

            TextField passwordField = new TextField("", skin);

            {
                passwordField.setPasswordMode(true);
                getContentTable().add(passwordField).pad(5);
                button("Join", true);
                button("Cancel", false);
            }
        };
        dialog.show(stage);
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

        batch.begin();
        batch.end();
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
