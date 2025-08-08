package org.example.Common.DataTransferObjects;

import org.example.Common.Player;

public class LoginPacket {
    public Player player;
    public String username;

    public LoginPacket() {}

    public LoginPacket(String username) {
        this.username = username;
    }
}
