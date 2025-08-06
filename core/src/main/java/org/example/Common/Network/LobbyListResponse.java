package org.example.Common.Network;

import org.example.Common.Lobby;
import java.io.Serializable;
import java.util.List;

public class LobbyListResponse implements Serializable {
    public List<Lobby> lobbies;

    public LobbyListResponse() {}

    public LobbyListResponse(List<Lobby> lobbies) {
        this.lobbies = lobbies;
    }
}
