package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.Enums.MessageType;
import org.example.Common.Network.LobbyListResponse;
import org.example.Common.Product;
import org.example.Common.Request.TradeMessage;
import org.example.Main;
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
            client.connect(5000, "192.168.1.52", 54555, 54777);
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
        kryo.register(MovePacket.class);
        kryo.register(PositionUpdate.class);
        kryo.register(TradeMessage.class);
        kryo.register(MessageType.class);
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
                }
                else if (object instanceof StoreUpdatePacket) {
                    StoreUpdatePacket p = (StoreUpdatePacket) object;
                    Gdx.app.postRunnable(() -> handleStoreUpdate(p));
                } else if (object instanceof ChatMessage) {
                    ChatMessage chat = (ChatMessage) object;
                    MyGame.getGameScreen().receiveChatMessage(chat.sender, chat.content);
                }
                else if (object instanceof  MarriageProposalReceived) {
                    MarriageProposalReceived msg  = (MarriageProposalReceived) object;
                    Gdx.app.postRunnable(() -> {
                        GameScreen screen = MenuNavigator.getGameScreen();
                        if (screen != null) screen.showProposalPopup(msg.fromPlayer);
                    });
                }
//                TradeMessage msg = (TradeMessage) object;

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
////                    case UPDATE:{
//////                        updateOfferSlot(msg.fromPlayer, msg.offerItem);
//////                        updateRequestSlot(msg.fromPlayer, msg.requestItem);
////
////                    }
//                }
            }
        });
    }



    private void handleStoreUpdate(StoreUpdatePacket p) {
        Store localStore = p.store;

        Screen current = Main.getMain().getScreen();
        if (current instanceof StoreView) {
            ((StoreView) current).rebuildItemList(localStore.getProducts());
        }
    }
    public void sendTCP(Object object) {
        GameClient.client.sendTCP(object);
    }



}
