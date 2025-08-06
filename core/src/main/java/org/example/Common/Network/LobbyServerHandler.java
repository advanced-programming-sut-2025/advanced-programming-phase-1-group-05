package org.example.Common.Network;

import com.esotericsoftware.kryonet.*;
import org.example.Common.Lobby;
import org.example.Common.Network.*;
import org.example.Common.Player;
import org.example.Server.managers.LobbyManager;

import java.util.*;

public class LobbyServerHandler extends Listener {

    private static final Map<String, Long> lobbyCreationTimestamps = new HashMap<>();
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000;

    @Override
    public void received(Connection c, Object object) {

        if (object instanceof CreateLobbyRequest) {
            CreateLobbyRequest req = (CreateLobbyRequest) object;
            Lobby lobby = LobbyManager.createLobby(req.name, req.isPrivate, req.password, req.visible, req.creator);
            lobbyCreationTimestamps.put(lobby.getId(), System.currentTimeMillis());
            System.out.println("✅ Lobby created: " + lobby.getId());
            c.sendTCP(new ResultResponse(true, "Lobby created with ID: " + lobby.getId()));
        }

        else if (object instanceof GetLobbiesRequest) {
            List<Lobby> active = LobbyManager.getActiveLobbies();
            c.sendTCP(new LobbyListResponse(active));
        }

        else if (object instanceof JoinLobbyRequest) {
            JoinLobbyRequest req = (JoinLobbyRequest) object;
            boolean joined = LobbyManager.joinLobby(req.lobbyId, req.player, req.password);
            if (joined) {
                c.sendTCP(new ResultResponse(true, "Joined lobby: " + req.lobbyId));
            } else {
                c.sendTCP(new ResultResponse(false, "Failed to join lobby: incorrect ID or password."));
            }
        }

        else if (object instanceof LeaveLobbyRequest) {
            LeaveLobbyRequest req = (LeaveLobbyRequest) object;
            LobbyManager.leaveLobby(req.player);
            c.sendTCP(new ResultResponse(true, "You left the lobby."));
        }
    }

    public static void cleanUpEmptyLobbies() {
        Iterator<Map.Entry<String, Long>> iterator = lobbyCreationTimestamps.entrySet().iterator();
        long now = System.currentTimeMillis();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            Lobby lobby = LobbyManager.getActiveLobbies().stream()
                .filter(l -> l.getId().equals(entry.getKey()))
                .findFirst().orElse(null);

            if (lobby == null || lobby.isEmpty() || now - entry.getValue() > EXPIRATION_TIME_MS) {
                iterator.remove();
                System.out.println("🗑 Removing expired/empty lobby: " + entry.getKey());
            }
        }
    }
}
