package org.example.models;

public class Trade {
    private final int id;
    private static int tradesCount = 0;
    private final Player player; // who initiated the trade
    private final Player targetPlayer; // who the offer request is for
    private final String type;
    private final Item item;
    private final int amount;
    private final Integer cost;
    private final Item targetItem;
    private final Integer targetAmount;
    private boolean answered = false;

    public Trade(Player player, Player targetPlayer, String type, Item item, int amount, Integer cost, Item targetItem, Integer targetAmount) {
        this.player = player;
        this.targetPlayer = targetPlayer;
        this.type = type;
        this.item = item;
        this.amount = amount;
        this.cost = cost;
        this.targetItem = targetItem;
        this.targetAmount = targetAmount;
        this.id = ++tradesCount;
    }

    public boolean isAnswered() {
        return answered;
    }

    public int getId() {
        return id;
    }

    public Player getSender(){
        return player;
    }

    public Player getReceiver() {
        return targetPlayer;
    }

    public String getType() {
        return type;
    }

    public Item getItem() {
        return item;
    }

    public int getAmount() {
        return amount;
    }

    public Integer getCost() {
        return cost;
    }

    public Item getTargetItem() {
        return targetItem;
    }

    public Integer getTargetAmount() {
        return targetAmount;
    }
}
