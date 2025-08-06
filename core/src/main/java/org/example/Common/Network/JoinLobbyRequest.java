package org.example.Common.Network;

import org.example.Common.Player;
import java.io.Serializable;

public class JoinLobbyRequest implements Serializable {
    public String lobbyId;
    public Player player;
    public String password;

    public JoinLobbyRequest() {}

    public JoinLobbyRequest(String lobbyId, Player player, String password) {
        this.lobbyId = lobbyId;
        this.player = player;
        this.password = password;
    }
}
