package org.example.Server.managers;

import org.example.Common.Lobby;
import org.example.Common.Network.SimplePlayer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LobbyManager {
    private static final List<Lobby> activeLobbies = new ArrayList<>();

    public static synchronized List<Lobby> getActiveLobbies() {
        System.out.println("Getting active lobbies!");
        Iterator<Lobby> iterator = activeLobbies.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().isEmpty()) {
                iterator.remove();
            }
        }
        return new ArrayList<>(activeLobbies);
    }

    public static synchronized Lobby createLobby(String name, boolean isPrivate, String password, boolean visible, SimplePlayer creator) {
        Lobby lobby = new Lobby(name, isPrivate, password, visible, creator);
        activeLobbies.add(lobby);
        System.out.println("lobby added");

        for (Lobby lobby1 : activeLobbies) {
            System.out.println(lobby1.getId());
        }
        return lobby;
    }

    public static synchronized boolean joinLobby(String lobbyId, SimplePlayer player, String password) {
        for (Lobby lobby : activeLobbies) {
            if (lobby.getId().equals(lobbyId)) {
                if (lobby.isPrivate() && (!lobby.getPassword().equals(password))) {
                    return false;
                }
                return lobby.addPlayer(player);
            }
        }
        return false;
    }

    public static synchronized void leaveLobby(SimplePlayer player) {
        for (Lobby lobby : activeLobbies) {
            if (lobby.getPlayers().contains(player)) {
                lobby.removePlayer(player);
            }
        }
    }


}
