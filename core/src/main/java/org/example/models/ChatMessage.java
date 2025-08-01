package org.example.models;

public class ChatMessage {
    private final String senderName;
    private final String receiverName; // برای پیام عمومی = null
    private final String text;
    private final boolean isPublic;

    public ChatMessage(String senderName, String receiverName, String text, boolean isPublic) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.text = text;
        this.isPublic = isPublic;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public String getText() {
        return text;
    }

    public boolean isPublic() {
        return isPublic;
    }
}
