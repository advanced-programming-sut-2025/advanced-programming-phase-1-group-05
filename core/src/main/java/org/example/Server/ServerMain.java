//package org.example.Server;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryonet.Connection;
//import com.esotericsoftware.kryonet.Listener;
//import com.esotericsoftware.kryonet.Server;
//import org.example.Common.DataTransferObjects.PlayerUpdate;
//import org.example.Common.Enums.MessageType;
//import org.example.Common.Request.TradeMessage;
//import org.example.Server.Packets.MovePacket;
//import org.example.Server.Packets.PositionUpdate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class ServerMain {
//    public static ServerGameState gameState = new ServerGameState();
//    private static final Map<String, Connection> playerConnections = new HashMap<>();
//
//    public static void main(String[] args) throws Exception {
//        Server server = new Server();
//        server.start();
//        server.bind(54555, 54777);
//
//        Kryo kryo = server.getKryo();
//        kryo.register(MovePacket.class);
//        kryo.register(PositionUpdate.class);
//        kryo.register(TradeMessage.class);
//        kryo.register(MessageType.class);
//
//        server.addListener(new Listener() {
//            public void received(Connection c, Object object) {
//                if (!(object instanceof TradeMessage)) return;
//
//                TradeMessage msg = (TradeMessage) object;
//                switch (msg.type) {
//                    case REQUEST : {
//                        // forward request to the target player
//                        Connection target = getConnectionByName(msg.toPlayer.getUsername());
//                        if (target != null) {
//                            target.sendTCP(msg);
//                        }
//                    }
//                    case RESPONSE : {
//                        Connection initiator = getConnectionByName(msg.toPlayer.getUsername());
//                        if (initiator != null) {
//                            if (msg.accepted) {
//                                // start trade
//                                TradeMessage start = new TradeMessage();
//                                start.type = MessageType.START;
//                                start.fromPlayer = msg.fromPlayer;
//                                start.toPlayer = msg.toPlayer;
//
//                                initiator.sendTCP(start);
//                                c.sendTCP(start); // send to responder too
//                            } else {
//                                TradeMessage rejected = new TradeMessage();
//                                rejected.type = MessageType.REJECTED;
//                                rejected.fromPlayer = msg.fromPlayer;
//                                rejected.toPlayer = msg.toPlayer;
//
//                                initiator.sendTCP(rejected);
//                            }
//                        }
//                    }
//                }
//            }
//
//            private Connection getConnectionByName(String playerName) {
//                return playerConnections.get(playerName);
//            }
//        });
//
//
//        System.out.println("Server started on port 54555!");
//    }
//}
package org.example.Server;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {
    public static void main(String[] args) {
        try {
            int port = 54555; // پورتی که کلاینت وصل میشه
            ServerSocket serverSocket = new ServerSocket(port);

            System.out.println("✅ Server started on port " + port);

            while (true) {
                // هر کلاینت که وصل شد
                Socket clientSocket = serverSocket.accept();
                System.out.println("🔗 New client connected: " + clientSocket.getInetAddress());

                // اجرای ClientHandler در یک Thread جدا
                Thread t = new Thread(new ClientHandler(clientSocket));
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("❌ Server failed to start: " + e.getMessage());
        }
    }
}
