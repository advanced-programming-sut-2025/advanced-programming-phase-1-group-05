package org.example.Common.DataTransferObjects;


public class PlayerUpdate {
    public String username;
    public float x;
    public float y;

    public PlayerUpdate() {}

    public PlayerUpdate(String username, float x, float y) {
        this.username = username;
        this.x = x;
        this.y = y;
    }
}
