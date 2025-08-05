package org.example.Common;

import java.io.Serializable;

public class Gift implements Serializable {
    Player sender;
    Player receiver;
    Item item;
    int amount;
    public int Id;
    int totalGiftsCount = 0;
    int rating = 0;
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

    public Item getItem() {
        return item;
    }

    public int getRating() {
        return  rating;
    }

    public boolean hasBeenRated() {
        return rating != 0;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
