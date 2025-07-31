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
    private List<User> players;
    private User admin;
    private long creationTime;

    public Lobby(String name, boolean isPrivate, String password, boolean isVisible, User creator) {
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
    public List<User> getPlayers() { return players; }
    public User getAdmin() { return admin; }
    public long getCreationTime() { return creationTime; }

    public void addPlayer(User user) {
        if(!players.contains(user)) players.add(user);
    }

    public void removePlayer(User user) {
        players.remove(user);
        if(players.isEmpty()) return;
        if(admin.equals(user)) admin = players.get(0);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }
}
