package org.example.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Lobby {
    private String id;
    private String name;
    private boolean isPrivate;
    private String password;
    private boolean isVisible;
    private List<Player> players;
    private Player admin;
    private long creationTime;

    public Lobby(String name, boolean isPrivate, String password, boolean isVisible, Player creator) {
        this.id = UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.name = name;
        this.isPrivate = isPrivate;
        this.password = password;
        this.isVisible = isVisible;
        this.players = new ArrayList<>();
        this.admin = creator;
        this.creationTime = System.currentTimeMillis();
        this.players.add(creator);
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public boolean isPrivate() { return isPrivate; }
    public boolean isVisible() { return isVisible; }
    public String getPassword() { return password; }
    public List<Player> getPlayers() { return players; }
    public Player getAdmin() { return admin; }
    public long getCreationTime() { return creationTime; }

    public void addPlayer(Player player) {
        if (!players.contains(player)) players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
        if (players.isEmpty()) return;
        if (admin.equals(player)) admin = players.get(0);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    @Override
    public String toString() {
        return name + " (" + players.size() + " players)";
    }
}
