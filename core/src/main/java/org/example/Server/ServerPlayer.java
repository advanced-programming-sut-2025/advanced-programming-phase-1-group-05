package org.example.Server;

import org.example.Common.ArtisanMachine;
import org.example.Common.Item;
import org.example.Server.models.Animal;

import java.util.List;

public class ServerPlayer {
    String id;
    float x, y;
    int gold;
    List<Item> inventory;
    boolean isOnline;
    List<Animal> animals;
    List<ArtisanMachine> machines;

    public ServerPlayer(String id) {
        this.id = id;
        this.x = 0;
        this.y = 0;
    }

    public String getId() {
        return id;
    }
}
