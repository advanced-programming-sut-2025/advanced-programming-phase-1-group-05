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

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import org.example.Common.DataTransferObjects.ChatMessage;
import org.example.Common.DataTransferObjects.PlayerUpdate;
import org.example.Common.Request.TradeMessage;
import org.example.Server.models.MyGame;
import org.example.Server.models.Result;

import java.io.IOException;

public class GameClient {
    public static Client client;
    public static void main(String[] args) throws IOException {
        client = new Client();
        client.start();

        Kryo kryo = client.getKryo();
        kryo.register(PlayerUpdate.class);
        kryo.register(TradeMessage.class);
        kryo.register(TradeMessage.MessageType.class);

        client.connect(5000, "192.168.1.52", 54555, 54777);  // change "localhost" to your server's IP if on LAN

        // Send initial position update
        PlayerUpdate update = new PlayerUpdate("friend-" + System.currentTimeMillis(), 0, 0);
        client.sendTCP(update);

        client.addListener(new Listener() {
            public void received(Connection c, Object object) {
                if (!(object instanceof TradeMessage)) return;
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
                }
            }
        });
    }
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


    // اگر خواستی متدهای دیگه‌ای مثل sendChatMessage هم اینجا اضافه کن
}
