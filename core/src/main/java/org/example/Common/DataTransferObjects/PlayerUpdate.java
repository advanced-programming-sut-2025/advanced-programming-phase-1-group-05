package org.example.Common.DataTransferObjects;


public class PlayerUpdate {
    public String playerId;
    public float x;
    public float y;

    public PlayerUpdate() {}

    public PlayerUpdate(String playerId, float x, float y) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
    }
}
