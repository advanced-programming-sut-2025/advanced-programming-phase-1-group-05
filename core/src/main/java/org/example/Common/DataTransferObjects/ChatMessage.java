package org.example.Common.DataTransferObjects;

public class ChatMessage {
    public String sender;
    public String receiver;
    public String content;

    public ChatMessage() {}

    public ChatMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }
    public ChatMessage(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

}
