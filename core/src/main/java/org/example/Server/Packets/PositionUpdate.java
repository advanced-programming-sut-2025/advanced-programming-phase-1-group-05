package org.example.Server.Packets;

public class PositionUpdate {
    public String playerUsername;
    public float x, y;

    public PositionUpdate() {}

    public PositionUpdate(String playerId, float x, float y) {
        this.playerUsername = playerId;
        this.x = x;
        this.y = y;
    }
}
