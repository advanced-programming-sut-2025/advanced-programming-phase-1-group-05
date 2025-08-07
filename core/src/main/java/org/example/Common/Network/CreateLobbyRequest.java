package org.example.Common.Network;

import org.example.Common.Player;

import java.io.Serializable;

public class CreateLobbyRequest implements Serializable {
    public String name;
    public boolean isPrivate;
    public String password;
    public boolean visible;
    public SimplePlayer creator;

    public CreateLobbyRequest() {}

    public CreateLobbyRequest(String name, boolean isPrivate, String password, boolean visible, SimplePlayer creator) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.password = password;
        this.visible = visible;
        this.creator = creator;
    }
}
