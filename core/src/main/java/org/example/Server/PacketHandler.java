package org.example.Server;

import org.example.Common.DataTransferObjects.GameMessage;
import org.example.Common.Network.*;
    import org.example.Server.managers.LobbyManager;

import java.io.ObjectOutputStream;

public class PacketHandler {

    public static void handle(Object message, ObjectOutputStream out) {
        try {
            // ---- درخواست لیست لابی‌ها ----
            if (message instanceof GetLobbiesRequest) {
                out.writeObject(new LobbyListResponse(LobbyManager.getActiveLobbies()));
            }

            // ---- ساخت لابی ----
            else if (message instanceof CreateLobbyRequest req) {
                LobbyManager.createLobby(req.name, req.isPrivate, req.password, req.visible, req.creator);
                out.writeObject(new ResultResponse(true, "Lobby created successfully!"));
            }

            // ---- جوین لابی ----
            else if (message instanceof JoinLobbyRequest req) {
                boolean ok = LobbyManager.joinLobby(req.lobbyId, req.player, req.password);
                if (ok)
                    out.writeObject(new ResultResponse(true, "Joined lobby successfully."));
                else
                    out.writeObject(new ResultResponse(false, "Lobby full or wrong password!"));
            }

            // ---- ترک لابی ----
            else if (message instanceof LeaveLobbyRequest req) {
                LobbyManager.leaveLobby(req.player);
                out.writeObject(new ResultResponse(true, "Left the lobby."));
            }

            // اگر پیام ناشناخته بود
            else {
                out.writeObject(new ResultResponse(false, "Unknown request type!"));
            }

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
