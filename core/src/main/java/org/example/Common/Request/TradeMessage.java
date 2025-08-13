package org.example.Common.Request;

import org.example.Common.Enums.MessageType;
import org.example.Common.Player;
import org.example.Common.Trade;

public class TradeMessage implements Message {
    public enum MessageType {
        REQUEST,RESPONSE,
        START,REJECTED,UPDATE, ACCEPT, DECLINE;
    }
    public MessageType type;
    public String fromPlayer;
    public String toPlayer;
    public Trade trade;
    public boolean accepted;

}
