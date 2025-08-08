package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.LoginPacket;
import org.example.Common.DataTransferObjects.PlayerUpdate;
import org.example.Common.DataTransferObjects.PrivateChatMessage;
import org.example.Common.Enums.Emote;
import org.example.Common.Enums.MessageType;
import org.example.Common.Lobby;
import org.example.Common.Network.*;
import org.example.Common.Player;
import org.example.Common.Product;
import org.example.Common.Request.TradeMessage;
import org.example.Main;
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
import org.example.Server.models.MyGame;
import org.example.Server.models.Result;
import org.example.Server.models.Store;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.esotericsoftware.kryonet.rmi.ObjectSpace.registerClasses;
import static org.example.Common.Request.TradeMessage.MessageType.UPDATE;

public class ClientNetworkManager {
    private Client client;
    private String username;

    public ClientNetworkManager() {
        client = new Client();
        registerClasses(client.getKryo());
        client.start();
        setupListeners();
        try {
            client.connect(5000, "192.168.1.56", 54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendTradeRequest(TradeMessage msg) {
        client.sendTCP(msg);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Client getClient() {
        return client;
    }
    private void registerClasses(com.esotericsoftware.kryo.Kryo kryo) {
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

    }

    public void setupListeners() {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof LobbyListResponse) {
                    Gdx.app.postRunnable(() -> {
                        LobbyListResponse res = (LobbyListResponse) object;
                        MenuNavigator.getLobbyMenu().updateLobbyList(res.lobbies);
                    });
                } else if (object instanceof StoreUpdatePacket) {
                    StoreUpdatePacket p = (StoreUpdatePacket) object;
                    Gdx.app.postRunnable(() -> handleStoreUpdate(p));
                }
                else if (object instanceof  MarriageProposalReceived) {
                    MarriageProposalReceived msg  = (MarriageProposalReceived) object;
                    Gdx.app.postRunnable(() -> {
                        GameScreen screen = MenuNavigator.getGameScreen();
                        if (screen != null) screen.showProposalPopup(msg.fromPlayer);
                    });
                }else if (object instanceof ChatMessage) {
                    ChatMessage chat = (ChatMessage) object;
                    MyGame.getGameScreen().receiveChatMessage(chat.sender, chat.content);
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

                    switch (msg.type) {
                        case REQUEST: {
                            boolean accepted = MyGame.getGameScreen().showTradingRequest(msg.fromPlayer.getUsername());

                            TradeMessage response = new TradeMessage();
                            response.type = TradeMessage.MessageType.RESPONSE;
                            response.fromPlayer = msg.toPlayer;
                            response.toPlayer = msg.fromPlayer;
                            response.accepted = accepted;

                            client.sendTCP(response);
                            break;
                        }

                        case START: {
                            //  MyGame.getGameScreen().startTradingWith(msg.toPlayer.getUsername());
                            break;
                        }

                        case REJECTED: {
                            Result result = new Result(false, "Your trade request was rejected :(");
                            MyGame.getGameScreen().showResult = true;
                            MyGame.getGameScreen().latestResult = result;
                            break;
                        }
                        case UPDATE: {
                            //updateOfferSlot(msg.fromPlayer, msg.offerItem);
                            //updateRequestSlot(msg.fromPlayer, msg.requestItem);

                        }
                        case ACCEPT:{
                            // TODO implement
                        }
                        case DECLINE: {
                            Main.getMain().setScreen(MyGame.getGameScreen());
                        }
                    }
                }
            }
        });
    }

    private void handleStoreUpdate(StoreUpdatePacket p) {
        Store localStore = MyGame.getDatabase().getStoreByName(p.storeName);

        Screen current = Main.getMain().getScreen();
        if (current instanceof StoreView) {
            ((StoreView) current).rebuildItemList(localStore.getProducts());
        }
    }
    public void sendTCP(Object object) {
        GameClient.client.sendTCP(object);
    }

    // setupListeners(), registerClasses(), etc.
}
