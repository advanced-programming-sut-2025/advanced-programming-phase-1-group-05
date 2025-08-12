package org.example.Server.Trade;

public class ServerTrade {
    public String fromPlayer;
    public String toPlayer;
    public String offeredItem;
    public int offeredAmount;
    public String requestedItem;
    public int requestedAmount;
    public boolean success;
    public ServerTrade(String fromPlayer, String toPlayer, String offeredItem, int offeredAmount) {
        this.fromPlayer = fromPlayer;
        this.toPlayer = toPlayer;
        this.offeredItem = offeredItem;
        this.offeredAmount = offeredAmount;
    }

}
