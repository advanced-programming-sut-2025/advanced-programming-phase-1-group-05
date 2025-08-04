package org.example.Common.Request;

import org.example.Common.Enums.MessageType;
import org.example.Common.Player;
import org.example.Common.Trade;

public class TradeMessage implements Message {
    public MessageType type;
    public Player fromPlayer;
    public Player toPlayer;
    public Trade trade;
    public boolean accepted;

}
