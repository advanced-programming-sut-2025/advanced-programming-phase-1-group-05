package org.example.Common.Network;

import org.example.Common.Player;
import java.io.Serializable;

public class LeaveLobbyRequest implements Serializable {
    public Player player;

    public LeaveLobbyRequest(Player player) {
        this.player = player;
    }
}
