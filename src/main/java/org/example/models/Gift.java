package org.example.models;

public class Gift {
    Player sender;
    Player receiver;
    Item item;
    int amount;
    int Id;
    int totalGiftsCount = 0;
    public Gift(Player sender, Player receiver, Item item, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.item = item;
        this.amount = amount;
        this.Id = ++totalGiftsCount;
    }

    public Player getReceiver() {
        return receiver;
    }

    public Player getSender() {
        return sender;
    }

    public int getAmount(){
        return  amount;
    }

    public String getName(){
        return item.getName();
    }

    public int getId(){
        return Id;
    }
}
