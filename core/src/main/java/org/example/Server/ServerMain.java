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
import org.example.Common.Lobby;
import org.example.Common.Network.*;
import org.example.Common.Player;
import org.example.Common.Product;
import org.example.Common.Request.TradeMessage;
import org.example.Server.Packets.MovePacket;
import org.example.Server.Packets.PositionUpdate;
import org.example.Server.Packets.PurchaseRequest;
import org.example.Server.Packets.StoreUpdatePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

public class ServerMain {
    public static ServerGameState gameState = new ServerGameState();
    private static final Map<String, Connection> playerConnections = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.start();
        server.bind(54555, 54777);

        Kryo kryo = server.getKryo();
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
                                    start.type = MessageType.START;
                                    start.fromPlayer = msg.fromPlayer;
                                    start.toPlayer = msg.toPlayer;

                                    initiator.sendTCP(start);
                                    c.sendTCP(start);
                                } else {
                                    TradeMessage rejected = new TradeMessage();
                                    rejected.type = MessageType.REJECTED;
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
                        updatePacket.store = request.store;

                        server.sendToAllTCP(updatePacket);
                    }
                }
                else if (object instanceof LoginPacket) {
                    LoginPacket login = (LoginPacket) object;
                    System.out.println("Registered player connection: " + login.username);
                    registerPlayerConnection(login.username, c);
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
            }

                private Connection getConnectionByName(String playerName) {
                    return playerConnections.get(playerName);
                }
            });
        server.addListener(new LobbyServerHandler());
        new java.util.Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                LobbyServerHandler.cleanUpEmptyLobbies();
            }
        }, 0, 60_000);

        System.out.println("Server started on port 54555!");
    }
    public static void registerPlayerConnection(String username, Connection connection) {
        playerConnections.put(username, connection);
    }

    public static Connection getConnectionByUsername(String username) {
        return playerConnections.get(username);
    }

}
