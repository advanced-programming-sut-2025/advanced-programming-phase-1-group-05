package org.example.Common.Network;

import org.example.Common.Player;
import java.io.Serializable;

public class LeaveLobbyRequest implements Serializable {
    public SimplePlayer player;

    public LeaveLobbyRequest() {}

    public LeaveLobbyRequest(SimplePlayer player) {
        this.player = player;
    }
}
