package org.example.Common;

import org.example.Common.Network.SimplePlayer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby implements Serializable {
    public static final int MAX_PLAYERS = 4;

    private String id;
    private String name;
    private boolean isPrivate;
    private String password;
    private boolean isVisible;
    private List<SimplePlayer> players = new ArrayList<>();
    private SimplePlayer admin;
    private long creationTime;

    public Lobby() {
        this.id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.creationTime = System.currentTimeMillis();
    }

    public Lobby(String name, boolean isPrivate, String password, boolean isVisible, SimplePlayer creator) {
        this();
        this.name = name;
        this.isPrivate = isPrivate;
        this.password = password;
        this.isVisible = isVisible;
        this.admin = creator;
        this.players.add(creator);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isPrivate() { return isPrivate; }
    public boolean isVisible() { return isVisible; }
    public String getPassword() { return password; }
    public List<SimplePlayer> getPlayers() { return players; }
    public SimplePlayer getAdmin() { return admin; }
    public long getCreationTime() { return creationTime; }

    public boolean addPlayer(SimplePlayer player) {
        if (players.size() >= MAX_PLAYERS) return false;
        if (!players.contains(player)) players.add(player);
        return true;
    }

    public void removePlayer(SimplePlayer player) {
        players.remove(player);
        if (!players.isEmpty() && admin.equals(player))
            admin = players.get(0);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    @Override
    public String toString() {
        return name + " (" + players.size() + " players)";
    }
}
