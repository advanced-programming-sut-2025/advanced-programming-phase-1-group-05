package org.example.models;

import java.util.*;

public class ChatModel {
    private final List<ChatMessage> publicMessages = new ArrayList<>();
    private final Map<String, List<ChatMessage>> privateMessages = new HashMap<>();

    public void addPublicMessage(String senderName, String text) {
        publicMessages.add(new ChatMessage(senderName, null, text, true));
    }

    public void addPrivateMessage(String sender, String receiver, String text) {
        String key = getConversationKey(sender, receiver);
        privateMessages.computeIfAbsent(key, k -> new ArrayList<>())
            .add(new ChatMessage(sender, receiver, text, false));
    }

    public List<ChatMessage> getPublicMessages() {
        return new ArrayList<>(publicMessages);
    }

    public List<ChatMessage> getPrivateMessages(String player1, String player2) {
        String key = getConversationKey(player1, player2);
        return privateMessages.containsKey(key)
            ? new ArrayList<>(privateMessages.get(key))
            : new ArrayList<>();
    }

    private String getConversationKey(String a, String b) {
        return a.compareTo(b) < 0 ? a + "_" + b : b + "_" + a;
    }
}
