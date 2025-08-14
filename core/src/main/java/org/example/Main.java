package org.example;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Client.*;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.LoginPacket;
import org.example.Common.DataTransferObjects.PrivateChatMessage;
import org.example.Common.Enums.Direction;
import org.example.Common.Enums.Emote;
import org.example.Common.Enums.Menu;
import org.example.Common.Enums.MessageType;
import org.example.Common.Lobby;
import org.example.Common.Network.*;
import org.example.Common.Player;
import org.example.Common.Request.TradeMessage;
import org.example.Common.Trade;
import org.example.Common.User;
import org.example.Server.Packets.EmotePackets.EmoteMessage;
import org.example.Server.Packets.EmotePackets.TextMessage;
import org.example.Server.Packets.MarriagePackets.MarriageProposalReceived;
import org.example.Server.Packets.MarriagePackets.MarriageProposalRequest;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResponse;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResult;
import org.example.Server.Packets.MovePacket;
import org.example.Server.Packets.OnlinePlayerPacket;
import org.example.Server.Packets.StartGamePacket;
import org.example.Server.controllers.DBController;
import org.example.Server.controllers.RegisterMenuController;
import org.example.Server.controllers.TradingController;
import org.example.Server.managers.LobbyManager;
import org.example.Server.models.MyGame;
import org.example.Server.models.Result;
import org.example.Server.models.UserDatabase;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

            GameClient.registerClasses(kryo);

            new Thread(() -> {
                try {
                    System.out.println("Attempting to connect...");

                    // Start a thread to call update() continuously
                    Thread updateThread = new Thread(() -> {
                        while (!GameClient.client.isConnected()) {
                            try {
                                GameClient.client.update(100); // 100ms delay
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                    updateThread.start();

                    // Connect (this blocks until connected or timeout)
                    GameClient.client.connect(5000, "192.168.1.54", 54555, 54777);

                    // Wait for update thread to finish
                    updateThread.join();

                    System.out.println("✅ Connected to server");

                    GameClient.addListeners();

                    if (Main.currentUser != null) {
                        LoginPacket loginPacket = new LoginPacket(Main.currentUser.getUsername());
                        GameClient.client.sendTCP(loginPacket);
                    }

                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

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
