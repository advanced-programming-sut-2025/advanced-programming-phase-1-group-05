package org.example.Common;

import java.io.Serializable;

public class Trade implements Serializable {
    public final int id;
    public static int tradesCount = 0;
    public final String player; // who initiated the trade
    public final String targetPlayer; // who the offer request is for
    public final String item;
    public final int amount;
    public final String targetItem;
    public final int targetAmount;
    public boolean answered = false;

    public Trade(String player, String targetPlayer, String item, int amount, String targetItem, int targetAmount) {
        this.player = player;
        this.targetPlayer = targetPlayer;
        this.item = item;
        this.amount = amount;
        this.targetItem = targetItem;
        this.targetAmount = targetAmount;
        this.id = ++tradesCount;
    }
}
