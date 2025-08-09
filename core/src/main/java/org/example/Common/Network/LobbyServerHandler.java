package org.example.Common.Network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import org.example.Common.Lobby;
import org.example.Server.managers.LobbyManager;

import java.util.*;

public class LobbyServerHandler extends Listener {

    private final Server server;
    private static final Map<String, Long> lobbyCreationTimestamps = new HashMap<>();
    private static final long EXPIRATION_TIME_MS = 5 * 60 * 1000;

    public LobbyServerHandler(Server server) {
        this.server = server;
    }

    @Override
    public void received(Connection c, Object object) {
        System.out.println("📦 Received object: " + object.getClass().getSimpleName());

        if (object instanceof CreateLobbyRequest) {
            CreateLobbyRequest req = (CreateLobbyRequest) object;
            Lobby lobby = LobbyManager.createLobby(req.name, req.isPrivate, req.password, req.visible, req.creator);
            lobbyCreationTimestamps.put(lobby.getId(), System.currentTimeMillis());

            System.out.println("✅ Lobby created: " + lobby.getId());
            System.out.println( "is private : " + lobby.isPrivate());
            c.sendTCP(new ResultResponse(true, "Lobby created with ID: " + lobby.getId()));

            broadcastLobbyList();
        }

        else if (object instanceof GetLobbiesRequest) {
            GetLobbiesRequest req = (GetLobbiesRequest) object;
            List<Lobby> visible = getVisibleLobbies(req.requester);
            c.sendTCP(new LobbyListResponse(visible));
        }

        else if (object instanceof JoinLobbyRequest) {
            JoinLobbyRequest req = (JoinLobbyRequest) object;
            boolean joined = LobbyManager.joinLobby(req.lobbyId, req.player, req.password);
            if (joined) {
                c.sendTCP(new ResultResponse(true, "Joined lobby: " + req.lobbyId));
                broadcastLobbyList();
            } else {
                c.sendTCP(new ResultResponse(false, "Failed to join lobby: incorrect ID or password."));
            }
        }

        else if (object instanceof LeaveLobbyRequest) {
            LeaveLobbyRequest req = (LeaveLobbyRequest) object;
            LobbyManager.leaveLobby(req.player);
            c.sendTCP(new ResultResponse(true, "You left the lobby."));
            broadcastLobbyList();
        }
    }

    private List<Lobby> getVisibleLobbies(SimplePlayer requester) {
        List<Lobby> all = LobbyManager.getActiveLobbies();
        List<Lobby> visible = new ArrayList<>();
        for (Lobby lobby : all) {
            if (lobby.isVisible() || lobby.getAdmin().equals(requester)) {
                visible.add(lobby);
            }
        }
        return visible;
    }

    private void broadcastLobbyList() {
        List<Lobby> visibleLobbies = new ArrayList<>();
        for (Lobby lobby : LobbyManager.getActiveLobbies()) {
            if (lobby.isVisible()) {
                visibleLobbies.add(lobby);
            }
        }
        server.sendToAllTCP(new LobbyListResponse(visibleLobbies));
    }

    public static void cleanUpEmptyLobbies() {
        Iterator<Map.Entry<String, Long>> iterator = lobbyCreationTimestamps.entrySet().iterator();
        long now = System.currentTimeMillis();
        //time for deleting lobbies
        long expirationTime = 5 * 60 * 1000;

        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            Lobby lobby = LobbyManager.getActiveLobbies().stream()
                .filter(l -> l.getId().equals(entry.getKey()))
                .findFirst()
                .orElse(null);

            if (lobby == null) {
                iterator.remove();
                continue;
            }

            if (lobby.isEmpty()) {
                iterator.remove();
                System.out.println("🗑 Removed empty lobby: " + lobby.getId());
                continue;
            }

            if (lobby.getPlayers().size() == 1 &&
                lobby.getPlayers().get(0).username.equals(lobby.getAdmin().username) &&
                now - entry.getValue() > expirationTime) {

                LobbyManager.leaveLobby(lobby.getAdmin());
                iterator.remove();
                System.out.println("⏱ Removed inactive lobby with only admin: " + lobby.getId());
            }
        }
    }

}
