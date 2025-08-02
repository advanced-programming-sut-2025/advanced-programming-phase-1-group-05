package org.example.Server;

import org.example.Common.DataTransferObjects.GameMessage;
import org.example.Server.Packets.PositionUpdate;

public class PacketHandler {

    public static void handle(GameMessage message, ClientHandler client) {
//        switch (message.type) {
//            case "MOVE" -> handleMove(message, client);
//            //case "PLANT" -> handlePlant(message, client);
//            // موارد دیگه...
//        }
    }

    private static void handleMove(GameMessage message, ClientHandler client) {
        ServerPlayer p = ServerMain.gameState.getPlayer(message.playerId);

        if (Math.abs(message.newX - p.x) > 10) return;

        p.x = message.newX;
        p.y = message.newY;

        // اطلاع به بقیه کلاینت‌ها
        //ServerMain.broadcastToAll(new PositionUpdate(p.id, p.x, p.y));
    }
}
