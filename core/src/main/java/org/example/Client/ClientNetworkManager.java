package org.example.Client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.Enums.MessageType;
import org.example.Common.Product;
import org.example.Common.Request.TradeMessage;
import org.example.Main;
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
            client.connect(5000, "localhost", 54555, 54777);
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
    }

    public void setupListeners() {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof StoreUpdatePacket) {
                    StoreUpdatePacket p = (StoreUpdatePacket) object;
                    Gdx.app.postRunnable(() -> handleStoreUpdate(p));
                } else if (object instanceof ChatMessage) {
                    ChatMessage chat = (ChatMessage) object;
                    MyGame.getGameScreen().receiveChatMessage(chat.sender, chat.content);
                }
                TradeMessage msg = (TradeMessage) object;

                switch (msg.type) {
                    case REQUEST: {
                        boolean accepted = MyGame.getGameScreen().showTradingRequest(msg.fromPlayer.getUsername());

                        TradeMessage response = new TradeMessage();
                        response.type = MessageType.RESPONSE;
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
                    case UPDATE:{
//                        updateOfferSlot(msg.fromPlayer, msg.offerItem);
//                        updateRequestSlot(msg.fromPlayer, msg.requestItem);

                    }
                }
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


    // setupListeners(), registerClasses(), etc.
}

//public class ClientNetworkManager {
//    private Socket socket;
//    private ObjectOutputStream out;
//    private ObjectInputStream in;
//
//    public ClientNetworkManager(String host, int port) throws Exception {
//        socket = new Socket(host, port);
//
//        // ✅ مهم: flush بعد از ساخت output stream
//        out = new ObjectOutputStream(socket.getOutputStream());
//        out.flush();
//
//        in = new ObjectInputStream(socket.getInputStream());
//
//        System.out.println("✅ Connected to server: " + host + ":" + port);
//    }
//
//    public synchronized Object sendAndReceive(Object request) {
//        try {
//            out.writeObject(request);
//            out.flush();
//            System.out.println("📤 Sending: " + request.getClass().getSimpleName());
//            return in.readObject();
//        } catch (Exception e) {
//            System.err.println("❌ Error in sendAndReceive:");
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public synchronized void send(Object request) {
//        try {
//            out.writeObject(request);
//            out.flush();
//        } catch (Exception e) {
//            System.err.println("❌ Error in send:");
//            e.printStackTrace();
//        }
//    }
//}
