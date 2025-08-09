package org.example.Server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.LoginPacket;
import org.example.Common.DataTransferObjects.PlayerUpdate;
import org.example.Common.DataTransferObjects.PrivateChatMessage;
import org.example.Common.Enums.MessageType;
import org.example.Common.Player;
import org.example.Common.Lobby;
import org.example.Common.Network.*;
import org.example.Common.Player;
import org.example.Common.Product;
import org.example.Common.Request.TradeMessage;
import org.example.Server.Packets.EmotePackets.EmoteMessage;
import org.example.Server.Packets.EmotePackets.TextMessage;
import org.example.Server.Packets.MarriagePackets.MarriageProposalReceived;
import org.example.Server.Packets.MarriagePackets.MarriageProposalRequest;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResponse;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResult;
import org.example.Server.Packets.MovePacket;
import org.example.Server.Packets.PositionUpdate;
import org.example.Server.Packets.PurchaseRequest;
import org.example.Server.Packets.StoreUpdatePacket;
import org.example.Server.controllers.DBController;
import org.example.Server.models.MyGame;
import org.w3c.dom.views.DocumentView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class ServerMain {
    public static ServerGameState gameState = new ServerGameState();
    private static final Map<Player, Connection> playerConnections = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
        server.bind(54555, 54777);

        Kryo kryo = server.getKryo();

        kryo.register(String.class);
        kryo.register(ArrayList.class);
        kryo.register(java.util.List.class);
        kryo.register(HashMap.class);

        kryo.register(PlayerUpdate.class);
        kryo.register(ChatMessage.class);
        kryo.register(PrivateChatMessage.class);
        kryo.register(TradeMessage.class);
        kryo.register(MessageType.class);
        kryo.register(LoginPacket.class);
        kryo.register(ResultResponse.class);

        kryo.register(Lobby.class);
        kryo.register(SimplePlayer.class);
        kryo.register(CreateLobbyRequest.class);
        kryo.register(GetLobbiesRequest.class);
        kryo.register(JoinLobbyRequest.class);
        kryo.register(LeaveLobbyRequest.class);
        kryo.register(LobbyListResponse.class);

        kryo.register(org.example.Server.models.Skills.AnimalCare.class);
        kryo.register(org.example.Server.models.Skills.Cooking.class);
        kryo.register(org.example.Server.models.Skills.Crafting.class);
        kryo.register(org.example.Server.models.Skills.Farming.class);
        kryo.register(org.example.Server.models.Skills.Fishing.class);
        kryo.register(org.example.Server.models.Skills.Foraging.class);
        kryo.register(org.example.Server.models.Skills.Mining.class);

        kryo.register(org.example.Server.models.Animal.class);
        kryo.register(org.example.Server.models.AnimalAnimations.class);
        kryo.register(org.example.Server.models.App.class);
        kryo.register(org.example.Server.models.Craft.class);
        kryo.register(org.example.Server.models.Database.class);
        kryo.register(org.example.Server.models.Farm.class);
        kryo.register(org.example.Server.models.Fish.class);
        kryo.register(org.example.Server.models.FishBar.class);
        kryo.register(org.example.Server.models.Food.class);
        kryo.register(org.example.Server.models.ForagingItem.class);
        kryo.register(org.example.Server.models.FruitAndVegetable.class);
        kryo.register(org.example.Server.models.GrowthStep.class);
        kryo.register(org.example.Server.models.Mineral.class);
        kryo.register(org.example.Server.models.MyGame.class);
        kryo.register(org.example.Server.models.NPC.class);
        kryo.register(org.example.Server.models.PacketHandler.class);
        kryo.register(org.example.Server.models.Result.class);
        kryo.register(org.example.Server.models.Store.class);
        kryo.register(org.example.Server.models.TileMapRenderer.class);
        kryo.register(org.example.Server.models.Tree.class);
        kryo.register(org.example.Server.models.UserDatabase.class);
        kryo.register(StoreUpdatePacket.class);
        kryo.register(PurchaseRequest.class);
        kryo.register(MarriageProposalRequest.class);
        kryo.register(MarriageProposalReceived.class);
        kryo.register(MarriageProposalResponse.class);
        kryo.register(MarriageProposalResult.class);
        kryo.register(OnlinePlayerPacket.class);


        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (object instanceof TradeMessage){
                    TradeMessage msg = (TradeMessage) object;
                    switch (msg.type) {
                        case REQUEST: {
                            Connection target = getConnectionByName(msg.toPlayer.getUsername());
                            if (target != null) {
                                target.sendTCP(msg);
                            }
                            break;
                        }

                        case RESPONSE: {
                            Connection initiator = getConnectionByName(msg.toPlayer.getUsername());
                            if (initiator != null) {
                                if (msg.accepted) {
                                    TradeMessage start = new TradeMessage();
                                    start.type = TradeMessage.MessageType.START;
                                    start.fromPlayer = msg.fromPlayer;
                                    start.toPlayer = msg.toPlayer;

                                    initiator.sendTCP(start);
                                    c.sendTCP(start);
                                } else {
                                    TradeMessage rejected = new TradeMessage();
                                    rejected.type = TradeMessage.MessageType.REJECTED;
                                    rejected.fromPlayer = msg.fromPlayer;
                                    rejected.toPlayer = msg.toPlayer;

                                    initiator.sendTCP(rejected);
                                }
                            }
                            break;
                        }
                    }
                }
                else if (object instanceof PurchaseRequest) {
                    PurchaseRequest request = (PurchaseRequest) object;
                    synchronized (gameState) {
                        StoreUpdatePacket updatePacket = new StoreUpdatePacket();
                        updatePacket.products = new ArrayList<>();
                        for (Map.Entry<Product, Integer> entry : request.items.entrySet()) {
                            updatePacket.products.add(entry.getKey());
                        }
                        updatePacket.storeName = request.storeName;

                        server.sendToAllTCP(updatePacket);
                    }
                }
                else if (object instanceof LoginPacket) {
                    LoginPacket login = (LoginPacket) object;
                    System.out.println("Registered player connection: " + login.username);
                    DBController.registerUser(login.player.getUser());
                    MyGame.getScoreboardView().setPlayers(DBController.getAllUsers());
                    registerPlayerConnection(login.player, c);
                    broadcastOnlinePlayers();
                }

                else if (object instanceof ChatMessage) {
                    ChatMessage chat = (ChatMessage) object;
                    if (chat.receiver != null && !chat.receiver.isBlank()) {
                        Connection target = getConnectionByUsername(chat.receiver);
                        if (target != null) {
                            target.sendTCP(chat);
                        } else {
                            System.out.println("❌ Target not found for private message: " + chat.receiver);
                        }
                    } else {
                        server.sendToAllTCP(chat);
                    }
                }
                else if (object instanceof EmoteMessage) {
                    EmoteMessage msg = (EmoteMessage) object;
                    for (Connection connection : server.getConnections()) {
                        connection.sendTCP(msg);
                    }
                }
                else if (object instanceof TextMessage) {
                    TextMessage message = (TextMessage) object;
                    for (Connection connection : server.getConnections()) {
                        connection.sendTCP(message);
                    }
                }
                else if (object instanceof MarriageProposalRequest) {
                    MarriageProposalRequest request = (MarriageProposalRequest) object;
                    MarriageProposalReceived msg = new MarriageProposalReceived();
                    msg.fromPlayer = request.fromPlayer;
                    server.sendToTCP(playerConnections.get(request.toPlayer.getUsername()).getID(), msg);
                }
                else if (object instanceof MarriageProposalResponse) {
                    MarriageProposalResponse response = (MarriageProposalResponse) object;
                    Player from = response.fromPlayer;
                    Player to = response.toPlayer;

                    MarriageProposalResult result = new MarriageProposalResult();
                    result.accepted = response.accepted;
                    result.byPlayer = from;
                    server.sendToTCP(playerConnections.get(to.getUsername()).getID(), result);
                    if (response.accepted) {
                        from.setSpouse(to);
                    }
                }
            }

            private Connection getConnectionByName(String playerName) {
                return playerConnections.get(playerName);
            }
            private String getUsernameFromConnection(Connection connection) {
                for(Map.Entry<Player, Connection> entry : playerConnections.entrySet()) {
                    if (entry.getValue() == connection) {
                        return entry.getKey().getUsername();
                    }
                }
                return "";
            }

            @Override
            public void disconnected(Connection connection) {
                String username = getUsernameFromConnection(connection);
                if (username != null) {
                    playerConnections.remove(username);
                    broadcastOnlinePlayers();
                }
            }

        });
        LobbyServerHandler lobbyHandler = new LobbyServerHandler(server);
        server.addListener(lobbyHandler);
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LobbyServerHandler.cleanUpEmptyLobbies();
            }
        }, 0, 60_000);

        System.out.println("Server started on port 54555!");
    }
    public static void registerPlayerConnection(Player player, Connection connection) {
        playerConnections.put(player, connection);
    }

    public static Connection getConnectionByUsername(String username) {
        return playerConnections.get(username);
    }
    public static void broadcastOnlinePlayers() {
        OnlinePlayerPacket packet = new OnlinePlayerPacket();
        packet.players = new ArrayList<>(playerConnections.keySet());
        for (Connection c : playerConnections.values()) {
            c.sendTCP(packet);
        }
    }



}
