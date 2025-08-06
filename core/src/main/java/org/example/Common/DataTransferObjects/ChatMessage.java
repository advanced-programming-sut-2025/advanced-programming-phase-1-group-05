package org.example.Common.DataTransferObjects;

public class ChatMessage {
    public String sender;
    public String content;

    public ChatMessage() {}

    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
}
