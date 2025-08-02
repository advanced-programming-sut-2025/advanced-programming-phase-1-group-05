package org.example.Server.Packets;

public class MovePacket {
    public String playerId;
    public float newX, newY;

    public MovePacket() {}

    public MovePacket(String playerId, float newX, float newY) {
        this.playerId = playerId;
        this.newX = newX;
        this.newY = newY;
    }
}
