package org.example.models;

public class Message {
    private final Player sender;
    private final Player receiver;
    private final String message;
    public Message(Player sender, Player receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }
    public Player getSender() {
        return sender;
    }
    public Player getReceiver() {
        return receiver;
    }
    public String getMessage() {
        return message;
    }
}
