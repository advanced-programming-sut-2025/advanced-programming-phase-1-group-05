package org.example.Common.Network;

import org.example.Common.Player;
import java.io.Serializable;

public class SimplePlayer implements Serializable {
    public String username;
    public String nickname;

    public SimplePlayer() {}

    public SimplePlayer(Player player) {
        this.username = player.getUsername();
        this.nickname = player.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimplePlayer that = (SimplePlayer) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    public String getUsername() {
        return username;
    }
}
