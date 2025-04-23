package org.example.controllers;

import org.example.models.*;

import java.util.HashMap;
import java.util.Map;


public class TradingController {
    private static Map<Player, OffersAndRequests> tradesByPlayer = new HashMap<>();


    public static Result trade(String input) {
        String targetUsername;
        int amount;
        Item item;
        String type;
        Player targetPlayer = Game.getPlayerByUsername(targetUsername);
        if (targetPlayer == null) return new Result(false, "Trying to trade with imaginary friends again?");
        if (Game.getCurrentPlayer().getItemQuantity(item) < amount) return new Result(false, "You're trying to give away more than you own. Generous… but impossible.");
        if (type.equals("offer request") || type.equals("request offer")) return new Result(false, "You're being a little too ambitious-choose to give or get, not both.");
        if (input.contains("-p") && input.contains("-ti")) return new Result(false, "This isn’t an auction house. You’ve gotta choose: gold or goods!");
    }

    public static void acceptTradingRequest(OffersAndRequests request, boolean accepted) {

    }



}