//package org.example.Client;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryonet.Client;
//import com.esotericsoftware.kryonet.Listener;
//import org.example.Common.DataTransferObjects.ChatMessage;
//import org.example.Common.DataTransferObjects.PlayerUpdate;
//import org.example.Common.Enums.MessageType;
//import org.example.Common.Request.TradeMessage;
//import org.example.Common.Trade;
//import org.example.Server.models.MyGame;
//import org.example.Server.models.Result;
//
//import java.io.IOException;
//import java.sql.Connection;
//
//public class GameClient {
//    static Client client;
//    public static void main(String[] args) throws IOException {
//        client = new Client();
//        client.start();
//
//        Kryo kryo = client.getKryo();
//        kryo.register(PlayerUpdate.class);
//        kryo.register(TradeMessage.class);
//        kryo.register(MessageType.class);
//        kryo.register(ChatMessage.class);
//
////        client.connect(5000, "localhost", 54555, 54777);  // change "localhost" to your server's IP if on LAN
//        client.connect(5000, "192.168.107.247", 54555, 54777);
//
//        // Send initial position update
//        PlayerUpdate update = new PlayerUpdate("friend-" + System.currentTimeMillis(), 0, 0);
//        client.sendTCP(update);
//
//        client.addListener(new Listener() {
//            public void received(Connection c, Object object) {
//                if (!(object instanceof TradeMessage)) return;
//                else if (object instanceof ChatMessage) {
//                    ChatMessage chat = (ChatMessage) object;
//                    MyGame.getGameScreen().receiveChatMessage(chat.sender, chat.content);
//                }
//                TradeMessage msg = (TradeMessage) object;
//
//                switch (msg.type) {
//                    case REQUEST: {
//                        boolean accepted = MyGame.getGameScreen().showTradingRequest(msg.fromPlayer.getUsername());
//
//                        TradeMessage response = new TradeMessage();
//                        response.type = MessageType.RESPONSE;
//                        response.fromPlayer = msg.toPlayer;
//                        response.toPlayer = msg.fromPlayer;
//                        response.accepted = accepted;
//
//                        client.sendTCP(response);
//                        break;
//                    }
//
//                    case START: {
//                      //  MyGame.getGameScreen().startTradingWith(msg.toPlayer.getUsername());
//                        break;
//                    }
//
//                    case REJECTED: {
//                        Result result = new Result(false, "Your trade request was rejected :(");
//                        MyGame.getGameScreen().showResult = true;
//                        MyGame.getGameScreen().latestResult = result;
//                        break;
//                    }
//                }
//            }
//        });
//
//
//
//        // Example: move right every second
////        new Thread(() -> {
////            try {
////                while (true) {
////                    Thread.sleep(1000);
////                    update.x += 1;
////                    client.sendTCP(update);
////                }
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }).start();
//    }
//
//    public void sendTradeRequest(TradeMessage msg) {
//        client.sendTCP(msg);
//    }
//}


package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
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
import org.example.Main;
import org.example.Server.Packets.*;
import org.example.Server.Packets.EmotePackets.EmoteMessage;
import org.example.Server.Packets.EmotePackets.TextMessage;
import org.example.Server.Packets.MarriagePackets.MarriageProposalReceived;
import org.example.Server.Packets.MarriagePackets.MarriageProposalRequest;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResponse;
import org.example.Server.Packets.MarriagePackets.MarriageProposalResult;
import org.example.Server.Packets.MovePacket;
import org.example.Server.Packets.OnlinePlayerPacket;
import org.example.Server.Packets.PositionUpdate;
import org.example.Server.Packets.ScoreboardUpdatePacket;
import org.example.Server.Packets.StartGamePacket;
import org.example.Server.Trade.TradePacket;
import org.example.Server.controllers.TradingController;
import org.example.Server.models.MyGame;
import org.example.Server.models.NPC;
import org.example.Server.models.Result;
import org.example.Server.models.ServerNPC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameClient {
    public static Client client;


    public void sendTradeRequest(org.example.Common.Request.TradeMessage msg) {
        if (client != null) {
            client.sendTCP(msg);
        }
    }

    public void sendChat(ChatMessage message) {
        if (client != null) {
            client.sendTCP(message);
        }
    }

    public static void addListeners() {
        client.addListener(new Listener() {
            public void connected(Connection connection) {
                System.out.println("✅ Connected to server");
            }

            public void disconnected(Connection connection) {
                System.out.println("❌ Disconnected from server");
            }
        });

        client.addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (object instanceof StartGamePacket) {
                    StartGamePacket startGamePacket = (StartGamePacket) object;
                    Gdx.app.postRunnable(() -> {
                        System.out.println(startGamePacket.lobbyId);
                        MenuNavigator.getLobbyMenu().startTheGame(startGamePacket.players);
                        for (Player player : MyGame.getAllPlayers()) {
                            System.out.println(player.getMapNum());
                        }
                        Main.getMain().setScreen(new GameScreen(MyGame.getAllPlayers()));

                    });
                }
                else if (object instanceof MovePacket) {
                    MovePacket movePacket = (MovePacket) object;
                    Player otherPlayer = MyGame.getPlayerByUsername(movePacket.playerUsername);
                    if (otherPlayer!= null) {
                        switch (movePacket.direction) {
                            case UP: {
                                otherPlayer.moveUp(movePacket.delta);
                                break;
                            }
                            case DOWN: {
                                otherPlayer.moveDown(movePacket.delta);
                                break;
                            }
                            case LEFT: {
                                otherPlayer.moveLeft(movePacket.delta);
                                break;
                            }
                            case RIGHT: {
                                otherPlayer.moveRight(movePacket.delta);
                                break;
                            }
                        }
                    }
                }
                else if (object instanceof PositionUpdate) {
                    PositionUpdate positionUpdate = (PositionUpdate) object;
                    Player otherPlayer = MyGame.getPlayerByUsername(positionUpdate.playerUsername);
                    otherPlayer.setPosition(positionUpdate.x, positionUpdate.y);
                }
                else if (object instanceof ChatMessage) {
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
                    Gdx.app.postRunnable(() -> {
                        if (MenuNavigator.getSharedSkin() != null && MenuNavigator.getLobbyMenu() != null) {
                            MenuNavigator.getLobbyMenu().updateLobbyList(response.lobbies);
                        }
                    });
                }

                else if (object instanceof ResultResponse) {
                    ResultResponse res = (ResultResponse) object;
                    System.out.println("✅ Lobby response: " + res.message);
                }
                else if (object instanceof EmoteMessage) {
                    EmoteMessage msg = (EmoteMessage) object;
                    Player player = MyGame.getPlayerByUsername(msg.senderUsername);
                    if (player != null)
                        player.triggerReaction(Emote.valueOf(msg.emoteName));
                }
                else if (object instanceof TextMessage) {
                    TextMessage msg = (TextMessage) object;
                    Player player = MyGame.getPlayerByUsername(msg.senderUsername);
                    if (player!= null) {
                        player.triggerReaction(msg.text);
                    }
                }
                else if (object instanceof TradeMessage) {
                    TradeMessage msg = (TradeMessage) object;
                    System.out.println("Trade message recieved from server");
                    System.out.println(msg.type);
                    System.out.println(msg.fromPlayer);
                    System.out.println(msg.toPlayer);
                    System.out.println(msg.trade);
                    switch (msg.type) {
                        case REQUEST:
                            MyGame.getGameScreen().showTradingRequest(msg.fromPlayer, accepted -> {
                                TradeMessage response = new TradeMessage();
                                response.type = TradeMessage.MessageType.RESPONSE;
                                response.fromPlayer = msg.toPlayer;
                                response.toPlayer = msg.fromPlayer;
                                response.accepted = accepted;

                                GameClient.client.sendTCP(response);
                            });
                            break;


                        case REJECTED: {
                            Result result = new Result(false, "Your trade request was rejected :(");
                            MyGame.getGameScreen().showResult = true;
                            MyGame.getGameScreen().latestResult = result;
                            break;
                        }
                        case START: {
                            boolean initiator;
                            if(msg.fromPlayer != null && msg.toPlayer.equals(MyGame.getCurrentPlayer().getUsername()))  {
                                initiator = true;
                            } else {
                                initiator = false;
                            }
                            Gdx.app.postRunnable(() -> {
                                MyGame.getGameScreen().controller.startTrading(msg, initiator);
                            });
                            break;
                        }
                        case UPDATE:{
                            Gdx.app.postRunnable(() -> {
                                TradingController controller = MyGame.getTradingController();
                                controller.updateOfferSlot(msg.trade);
                                controller.updateRequestSlot(msg.trade);
                            });
                            break;
                        }
                        case ACCEPT:{
                            Gdx.app.postRunnable(() -> {
                                MyGame.getTradingController()
                                    .completeTrade(msg);
                            });
                            break;
                        }
                        case DECLINE: {
                            Gdx.app.postRunnable(() -> {
                                MyGame.getTradingController().rejectTrade(msg.fromPlayer, msg.toPlayer);
                                Main.getMain().setScreen(MyGame.getGameScreen());
                            });
                            break;
                        }

                    }

                }
                else if (object instanceof OnlinePlayerPacket) {
                    OnlinePlayerPacket packet = (OnlinePlayerPacket) object;
                    Gdx.app.postRunnable(() -> {
                        List<Player> players = new ArrayList<>();
                        for (String playerUsername : packet.players) {
                            players.add(MyGame.getPlayerByUsername(playerUsername));
                        }
                        MainMenu.updateOnlinePlayers(players);
                    });
                }
                else if (object instanceof HugMessage) {
                    HugMessage message = (HugMessage) object;
                    Player playerA = MyGame.getPlayerByUsername(message.player1);
                    Player playerB = MyGame.getPlayerByUsername(message.player2);
                    Gdx.app.postRunnable(() -> {
                        GameScreen screen = MenuNavigator.getGameScreen();
                        if (screen!= null) {
                            screen.hug(playerA, playerB);
                        }
                    });
                }
                else if (object instanceof MarriageProposalReceived) {
                    MarriageProposalReceived msg  = (MarriageProposalReceived) object;
                    Gdx.app.postRunnable(() -> {
                        GameScreen screen = MenuNavigator.getGameScreen();
                        if (screen != null) screen.showProposalPopup(MyGame.getPlayerByUsername(msg.fromPlayer));
                    });
                } else if(object instanceof ScoreboardUpdatePacket) {
                    ScoreboardUpdatePacket packet = (ScoreboardUpdatePacket) object;
                    System.out.println("📊 Scoreboard update received: " + packet.username + " G:" + packet.gold);
                    Gdx.app.postRunnable(() -> {
                        MyGame.getScoreboardView().updateUser(packet);
                    });
                }
                else if (object instanceof  NpcMovePacket) {
                    NpcMovePacket packet = (NpcMovePacket) object;
                    //System.out.println("hello");
                    Gdx.app.postRunnable(() -> {
                        NpcActor npcActor = MyGame.getNpcActorByName(packet.npcName);
                        if (npcActor != null) {
                            npcActor.setPosition(packet.x, packet.y);
                        }
                        else {
                            //System.out.println("null lol");
                        }
                        NPC npc  = MyGame.getNPCByName(packet.npcName);
                        if (npc != null) {
                            npc.setPosition(packet.x, packet.y);
                            npc.direction = packet.direction;
                            npc.stateTime += packet.delta;
                        }
                        else {
                           // System.out.println("null again🤣🤣");
                        }
                    });
                }

            }
        });
    }

    public static void registerClasses(com.esotericsoftware.kryo.Kryo kryo) {
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
    }
}
