package org.example.Common;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private final Player sender;
    private final List<Player> receivers = new ArrayList<>();
    private final String message;
    public Message(Player sender, List<Player> receivers, String message) {
        this.sender = sender;
        this.receivers.addAll(receivers);
        this.message = message;
    }
    public Player getSender() {
        return sender;
    }
    public List<Player> getReceiver() {
        return receivers;
    }
    public String getMessage() {
        return message;
    }
}
