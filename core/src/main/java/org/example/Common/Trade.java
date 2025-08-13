package org.example.Common;

import java.io.Serializable;

public class Trade implements Serializable {
    public int id;
    public static int tradesCount = 0;
    public String player; // who initiated the trade
    public String targetPlayer; // who the offer request is for
    public String item;
    public int amount;
    public String targetItem;
    public int targetAmount;
    public boolean answered = false;

    public Trade() {

    }


}
