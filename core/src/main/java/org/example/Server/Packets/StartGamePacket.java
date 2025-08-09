package org.example.Server.Packets;

import org.example.Common.Network.SimplePlayer;

import java.util.List;

public class StartGamePacket {
    public List<SimplePlayer> players;
    public String lobbyId;
}
