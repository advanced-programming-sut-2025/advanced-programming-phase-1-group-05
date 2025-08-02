package org.example.Server.Packets;

public class PositionUpdate {
    public String playerId;
    public float x, y;

    public PositionUpdate() {}

    public PositionUpdate(String playerId, float x, float y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }
}
