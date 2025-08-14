package org.example.Server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.example.Client.NpcActor;
import org.example.Common.*;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.LoginPacket;
import org.example.Common.DataTransferObjects.PrivateChatMessage;
import org.example.Common.Enums.Direction;
import org.example.Common.Enums.MessageType;
import org.example.Common.Network.*;
import org.example.Common.Request.TradeMessage;
import org.example.Common.Tool.BackPack;
import org.example.Server.Packets.*;
import org.example.Server.Packets.EmotePackets.EmoteMessage;
import org.example.Server.Packets.EmotePackets.TextMessage;
import org.example.Server.Packets.MarriagePackets.MarriageProposalReceived;
import org.example.Server.Packets.MarriagePackets.MarriageProposalRequest;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResponse;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResult;
import org.example.Server.Trade.TradePacket;
import org.example.Server.controllers.NpcController;
import org.example.Server.models.MyGame;
import org.example.Server.models.ServerNPC;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerMain {
    private static final Map<String, Connection> playerConnections = new HashMap<>();
    private static Server server;
    private static List<NpcController> npcControllers = new ArrayList<>();
    static boolean gameStarted = false;
    private static TimeAndDate timeAndDate;
    public static void main(String[] args) throws Exception {
        server = new Server();
        server.start();
        server.bind(54555, 54777);
        initializeNPCs();
        startGameLoop();
        Kryo kryo = server.getKryo();

        kryo.register(String.class);
        kryo.register(ArrayList.class);
        kryo.register(java.util.List.class);
        kryo.register(HashMap.class);

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
        kryo.register(MarriageProposalRequest.class);
        kryo.register(MarriageProposalReceived.class);
        kryo.register(MarriageProposalResponse.class);
        kryo.register(MarriageProposalResult.class);
        kryo.register(EmoteMessage.class);
        kryo.register(TextMessage.class);
        kryo.register(OnlinePlayerPacket.class);
        kryo.register(StartGamePacket.class);
        kryo.register(MovePacket.class);
        kryo.register(Direction.class);
        kryo.register(ScoreboardUpdatePacket.class);
        kryo.register(NpcMovePacket.class);
        kryo.register(ServerNPC.class);
        kryo.register(TradePacket.class);
        kryo.register(Trade.class);
        kryo.register(TradeMessage.MessageType.class);
        kryo.register(PositionUpdate.class);
        kryo.register(HugMessage.class);
        kryo.register(PurchaseRequest.class);
        kryo.register(TimePacket.class);
        kryo.register(NotificationPacket.class);

        server.addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (object instanceof StartGamePacket) {

                    StartGamePacket msg = (StartGamePacket) object;
                    for (SimplePlayer player : msg.players) {
                        Connection playerConn = playerConnections.get(player.getUsername());
                        if (playerConn != null) {
                            StartGamePacket packet = new StartGamePacket();
                            packet.lobbyId = msg.lobbyId;
                            packet.players = new ArrayList<>();
                            packet.players.addAll(msg.players);
                            playerConn.sendTCP(packet);
                        }
                    }
                    gameStarted = true;
                    timeAndDate = new TimeAndDate();
                } else if (object instanceof MovePacket) {
                    MovePacket movePacket = (MovePacket) object;
                    server.sendToAllExceptTCP(c.getID(), movePacket);
                }
                else if (object instanceof PositionUpdate) {
                    PositionUpdate positionUpdate = (PositionUpdate) object;
                    server.sendToAllExceptTCP(c.getID(), positionUpdate);
                }
                else if (object instanceof TradeMessage){
                    TradeMessage msg = (TradeMessage) object;
                    System.out.println("Trade message recieved from client");
                    System.out.println(msg.type);
                    System.out.println(msg.fromPlayer);
                    System.out.println(msg.toPlayer);
                    System.out.println(msg.trade);
                    switch (msg.type) {
                        case REQUEST: {
                            Connection target = getConnectionByName(msg.toPlayer);
                            if (target != null) {
                                target.sendTCP(msg);
                            }
                            break;
                        }

                        case RESPONSE: {
                            Connection initiator = getConnectionByName(msg.toPlayer);
                            System.out.println(initiator);
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
//                    synchronized (gameState) {
//                        StoreUpdatePacket updatePacket = new StoreUpdatePacket();
//                        updatePacket.products = new ArrayList<>();
//                        for (Map.Entry<Product, Integer> entry : request.items.entrySet()) {
//                            updatePacket.products.add(entry.getKey());
//                        }
//                        updatePacket.storeName = request.storeName;
//
//                        server.sendToAllTCP(updatePacket);
//                    }
                }
                else if (object instanceof LoginPacket) {
                    LoginPacket login = (LoginPacket) object;
                    System.out.println("Registered player connection: " + login.username);
                    registerPlayerConnection(login.username, c);
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
                else if (object instanceof  NotificationPacket) {
                    NotificationPacket packet = (NotificationPacket) object;
                    server.sendToTCP(playerConnections.get(packet.receiverUsername).getID(), packet);
                }
                else if (object instanceof  HugMessage) {
                    HugMessage msg = (HugMessage) object;
                    server.sendToAllExceptTCP(c.getID(), msg);
                }
                else if (object instanceof MarriageProposalRequest) {
                    MarriageProposalRequest request = (MarriageProposalRequest) object;
                    MarriageProposalReceived msg = new MarriageProposalReceived();
                    msg.fromPlayer = request.fromPlayer;
                    server.sendToTCP(playerConnections.get(request.toPlayer).getID(), msg);
                }
                else if (object instanceof  MarriageProposalResponse) {
                    MarriageProposalResponse response = (MarriageProposalResponse) object;
                    Player from = MyGame.getPlayerByUsername(response.fromPlayer);
                    Player to = MyGame.getPlayerByUsername(response.toPlayer);

                    MarriageProposalResult result = new MarriageProposalResult();
                    result.accepted = response.accepted;
                    result.byPlayer = from.getUsername();
                    server.sendToTCP(playerConnections.get(to.getUsername()).getID(), result);
                    if (response.accepted) {
                        from.setSpouse(to);
                    }
                } else if(object instanceof ScoreboardUpdatePacket) {
                    ScoreboardUpdatePacket update = (ScoreboardUpdatePacket) object;
                    server.sendToAllTCP(update);
                }
            }

                private Connection getConnectionByName(String playerName) {
                    return playerConnections.get(playerName);
                }
               private String getUsernameFromConnection(Connection connection) {
                for(Map.Entry<String, Connection> entry : playerConnections.entrySet()) {
                    if (entry.getValue() == connection) {
                        return entry.getKey();
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
    public static void registerPlayerConnection(String username, Connection connection) {
        playerConnections.put(username, connection);
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

    public static Server getServer(){
        return server;
    }

    private static void startGameLoop() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                {
                    if (gameStarted){
                        float delta = 0.05f;
                        timeAndDate.advanceTime(delta);
                        TimePacket packet = new TimePacket();
                        packet.hour = timeAndDate.hour;
                        server.sendToAllTCP(packet);
                        for (NpcController controller : npcControllers) {
                            controller.update(delta, timeAndDate.hour);

                            NpcMovePacket pkt = new NpcMovePacket();
                            pkt.npcName = controller.getNpc().name;
                            pkt.x = controller.getNpc().x;
                            pkt.y = controller.getNpc().y;
                            pkt.direction = controller.getNpc().direction;
                            pkt.delta = delta;

                            server.sendToAllTCP(pkt);
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    private static void initializeNPCs() {

        npcControllers.add(new NpcController(new ServerNPC("Sebastian", true, 10, 17, 3450, 4000, 6493.93f, 4108)));
        npcControllers.add(new NpcController(new ServerNPC("Abigail", true, 10, 16, 7100, 4050, 4470.5f, 4468.5f)));
        npcControllers.add(new NpcController(new ServerNPC("Harvey", true, 10, 17, 4650, 3150, 6404.52f, 3929.28f)));
        npcControllers.add(new NpcController(new ServerNPC("Leah", true, 10, 16, 2350, 4300, 1129.33f, 3902.43f)));
        npcControllers.add(new NpcController(new ServerNPC("Robin", true, 10, 20, 1120, 3950, 156.41f, 4823)));


    }
}
