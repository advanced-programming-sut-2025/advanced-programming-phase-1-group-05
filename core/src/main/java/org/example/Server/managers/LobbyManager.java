package org.example.Server.managers;

import org.example.Common.Lobby;
import org.example.Common.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LobbyManager {
    private static final List<Lobby> activeLobbies = new ArrayList<>();

    public static synchronized List<Lobby> getActiveLobbies() {
        // قبل از برگشت، لابی‌های خالی رو حذف کن
        Iterator<Lobby> iterator = activeLobbies.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isEmpty()) {
                iterator.remove();
            }
        }
        return new ArrayList<>(activeLobbies);
    }

    public static synchronized Lobby createLobby(String name, boolean isPrivate, String password, boolean visible, Player creator) {
        Lobby lobby = new Lobby(name, isPrivate, password, visible, creator);
        activeLobbies.add(lobby);
        return lobby;
    }

    public static synchronized boolean joinLobby(String lobbyId, Player player, String password) {
        for (Lobby lobby : activeLobbies) {
            if (lobby.getId().equals(lobbyId)) {
                if (lobby.isPrivate() && (password == null || !lobby.getPassword().equals(password))) {
                    return false;
                }
                return lobby.addPlayer(player);
            }
        }
        return false;
    }

    public static synchronized void leaveLobby(Player player) {
        for (Lobby lobby : activeLobbies) {
            lobby.removePlayer(player);
        }
    }
}
