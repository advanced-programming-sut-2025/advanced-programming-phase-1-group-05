package org.example.models;

public class Trade {
    private Player player; // who initiated the trade
    private Player targetPlayer; // who the offer request is for
    private String type;
    private Item item;
    private int amount;
    private Integer cost;
    private Item targetItem;
    private Integer targetAmount;

    public Trade(Player player, Player targetPlayer, String type, Item item, int amount, Integer cost, Item targetItem, Integer targetAmount) {

    }
}
