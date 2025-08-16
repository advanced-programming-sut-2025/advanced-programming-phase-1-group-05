package org.example.Server.Packets;

import org.example.Common.SavePlayer;

import java.util.List;

public class ContinueGamePacket {
    public List<SavePlayer> players;
    public String lobbyId;
}
