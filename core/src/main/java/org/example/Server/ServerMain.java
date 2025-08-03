//package org.example.Server;
//
//import com.esotericsoftware.kryo.Kryo;
//import com.esotericsoftware.kryonet.Connection;
//import com.esotericsoftware.kryonet.Listener;
//import com.esotericsoftware.kryonet.Server;
//import org.example.Common.DataTransferObjects.PlayerUpdate;
//import org.example.Server.Packets.MovePacket;
//import org.example.Server.Packets.PositionUpdate;
//
//public class ServerMain {
//    public static ServerGameState gameState = new ServerGameState();
//
//    public static void main(String[] args) throws Exception {
//        Server server = new Server();
//        server.start();
//        server.bind(54555, 54777);
//
//        Kryo kryo = server.getKryo();
//        kryo.register(MovePacket.class);
//        kryo.register(PositionUpdate.class);
//
//        server.addListener(new Listener() {
//            @Override
//            public void received(Connection connection, Object object) {
//                if (object instanceof MovePacket) {
//                    MovePacket move = (MovePacket) object;
//                    ServerPlayer player = gameState.getPlayer(move.playerId);
//                    if (player == null) {
//                        player = new ServerPlayer(move.playerId);
//                        gameState.addPlayer(player);
//                    }
//                    player.x = move.newX;
//                    player.y = move.newY;
//
//                    PositionUpdate update = new PositionUpdate(player.id, player.x, player.y);
//                    server.sendToAllExceptTCP(connection.getID(), update);
//                }
//            }
//        });
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
