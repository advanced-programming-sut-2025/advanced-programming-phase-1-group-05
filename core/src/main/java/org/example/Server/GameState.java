package org.example.Server;

import java.util.HashMap;
import java.util.Map;

public class GameState {
    private Map<String, ServerPlayer> players = new HashMap<>();

    public ServerPlayer getPlayer(String playerId) {
        return players.get(playerId);
    }

    public void addPlayer(ServerPlayer player) {
        players.put(player.getId(), player);
    }

    // more methods for managing players
}
