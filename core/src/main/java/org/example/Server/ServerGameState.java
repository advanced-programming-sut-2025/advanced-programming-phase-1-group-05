package org.example.Server;

import org.example.Common.Player;

import javax.swing.text.PlainDocument;
import java.util.HashMap;
import java.util.Map;

public class ServerGameState {
    Map<String, ServerPlayer> players = new HashMap<>();
    long currentTime;

    public ServerPlayer getPlayer(String id) {
        return players.get(id);
    }

    public void addPlayer(ServerPlayer player) {
        players.put(player.getId(), player);
    }
}
