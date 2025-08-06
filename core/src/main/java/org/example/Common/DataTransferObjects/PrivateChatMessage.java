package org.example.Common.DataTransferObjects;

public class PrivateChatMessage {
    public String sender;
    public String receiver;
    public String content;

    public PrivateChatMessage() {}

    public PrivateChatMessage(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }
}
