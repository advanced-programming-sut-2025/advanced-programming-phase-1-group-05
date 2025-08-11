package org.example.Server.models;

import org.example.Common.Enums.Direction;

public class ServerNPC {
    public String name;
    public boolean walking;
    public int startHour;
    public int endHour;
    public float storeX;
    public float storeY;
    public float x;
    public float y;
    public Direction direction = Direction.DOWN;

    public ServerNPC() {

    }

    public ServerNPC(String name, boolean walking, int startHour, int endHour, float storeX, float storeY, float x, float y) {
        this.name = name;
        this.walking = walking;
        this.startHour = startHour;
        this.endHour = endHour;
        this.storeX = storeX;
        this.storeY = storeY;
        this.x = x;
        this.y = y;
    }
}
