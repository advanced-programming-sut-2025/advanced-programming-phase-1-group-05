package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Client.ClientNetworkManager;
import org.example.Client.GameClient;
import org.example.Client.MenuNavigator;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.LoginPacket;
import org.example.Common.DataTransferObjects.PlayerUpdate;
import org.example.Common.DataTransferObjects.PrivateChatMessage;
import org.example.Common.Enums.MessageType;
import org.example.Common.Lobby;
import org.example.Common.Network.*;
import org.example.Common.Player;
import org.example.Common.Request.TradeMessage;
import org.example.Common.User;
import org.example.Server.controllers.DBController;
import org.example.Server.controllers.RegisterMenuController;
import org.example.Server.models.MyGame;
import org.example.Server.models.Result;
import org.example.Server.models.UserDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends Game {
    public static SpriteBatch batch;
    private Skin skin;
    private static Main main;
    public ClientNetworkManager networkManager;
    public GameClient gameClient;
    public static User currentUser;

    public static Main getMain() {
        return main;
    }

    public static SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void create() {
        main = this;

        // ✅ Init network connection (KryoNet Client)
        if (GameClient.client == null) {
            GameClient.client = new Client();
            GameClient.client.start();

            Kryo kryo = GameClient.client.getKryo();
            kryo.register(PlayerUpdate.class);
            kryo.register(TradeMessage.class);
            kryo.register(MessageType.class);
            kryo.register(ChatMessage.class);
            kryo.register(PrivateChatMessage.class);
            kryo.register(String.class);
            kryo.register(LoginPacket.class);
            kryo.register(Lobby.class);
            kryo.register(Player.class);
            kryo.register(ArrayList.class);
            kryo.register(java.util.List.class);
            kryo.register(HashMap.class);
            kryo.register(CreateLobbyRequest.class);
            kryo.register(GetLobbiesRequest.class);
            kryo.register(JoinLobbyRequest.class);
            kryo.register(LeaveLobbyRequest.class);
            kryo.register(LobbyListResponse.class);
            kryo.register(ResultResponse.class);
            kryo.register(org.example.Server.models.Skills.AnimalCare.class);



            try {
                GameClient.client.connect(5000, "192.168.107.247", 54555, 54777); // ← IP سرور
            } catch (IOException e) {
                e.printStackTrace();
            }

            GameClient.client.addListener(new Listener() {
                public void received(Connection c, Object object) {
                    if (object instanceof ChatMessage) {
                        ChatMessage chat = (ChatMessage) object;
                        String currentUser = MyGame.getCurrentPlayer().getUsername();

                        if (chat.receiver == null || chat.receiver.isBlank()) {
                            MyGame.getGameScreen().receiveChatMessage(chat.sender, chat.content);
                        } else if (chat.receiver.equals(currentUser)) {
                            MyGame.getGameScreen().receivePrivateMessage(chat.sender, chat.content);
                        }
                    }
                    else if (object instanceof LobbyListResponse) {
                        LobbyListResponse response = (LobbyListResponse) object;
                        if (MenuNavigator.getSharedSkin() != null && MenuNavigator.getLobbyMenu() != null) {
                            MenuNavigator.getLobbyMenu().updateLobbyList(response.lobbies);
                        }
                    }

                    else if (object instanceof ResultResponse) {
                        ResultResponse res = (ResultResponse) object;
                        System.out.println("✅ Lobby response: " + res.message);
                        // می‌تونی اینو به UI هم پاس بدی
                    }
                    else if (object instanceof TradeMessage) {
                        TradeMessage msg = (TradeMessage) object;

                        switch (msg.type) {
                            case REQUEST:
                                boolean accepted = MyGame.getGameScreen().showTradingRequest(msg.fromPlayer.getUsername());

                                TradeMessage response = new TradeMessage();
                                response.type = MessageType.RESPONSE;
                                response.fromPlayer = msg.toPlayer;
                                response.toPlayer = msg.fromPlayer;
                                response.accepted = accepted;

                                GameClient.client.sendTCP(response);
                                break;

                            case REJECTED:
                                Result result = new Result(false, "Your trade request was rejected :(");
                                MyGame.getGameScreen().showResult = true;
                                MyGame.getGameScreen().latestResult = result;
                                break;

                            case START:
                                // Optional: handle trade start
                                break;
                        }

                    }
                }
            });
        }

        // 🎮 UI and Game Setup
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

        MenuNavigator.init(this, skin);

        networkManager = new ClientNetworkManager();
        checkAutoLogin();

        if (currentUser != null) {
            System.out.println("kkkkk");
            LoginPacket loginPacket = new LoginPacket(currentUser.getUsername());
            GameClient.client.sendTCP(loginPacket);
        }

        MenuNavigator.showMainMenu();
    }

    public static void checkAutoLogin() {
        DBController.loadPlayersFromFile();
        UserDatabase.loadUsers();
        File file = new File("currentuser.json");

        if (file == null || !file.exists() || file.length() == 0) {
            MenuNavigator.showLoginMenu();
            return;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get("currentuser.json")));
            String username = extractValue(content, "username");
            String stayLoggedStr = extractValue(content, "stayLogged");

            boolean stayLogged = Boolean.parseBoolean(stayLoggedStr);

            if (stayLogged && username != null) {
                User user = UserDatabase.getUserByUsername(username);
                if (user != null) {
                    currentUser = user;
                    RegisterMenuController.currentUser = user;

                    Player currentPlayer = MyGame.getPlayerByUsername(user.getUsername());

                    MyGame.setCurrentPlayer(currentPlayer);

                    MenuNavigator.showMainMenu();
                    return;
                }
            } else {
                MenuNavigator.showLoginMenu();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        MenuNavigator.showLoginMenu();
    }

    public ClientNetworkManager getNetworkManager() {
        return networkManager;
    }

    private static String extractValue(String json, String key) {
        int index = json.indexOf("\"" + key + "\"");
        if (index == -1) return null;

        int colonIndex = json.indexOf(":", index);
        int startQuote = json.indexOf("\"", colonIndex + 1);
        int endQuote = json.indexOf("\"", startQuote + 1);

        if (startQuote == -1 || endQuote == -1) {
            String part = json.substring(colonIndex + 1).trim();
            if (part.startsWith("true")) return "true";
            if (part.startsWith("false")) return "false";
            return null;
        }

        return json.substring(startQuote + 1, endQuote);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        skin.dispose();
    }
}
