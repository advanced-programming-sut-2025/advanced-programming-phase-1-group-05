package org.example.Server.controllers;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.example.Client.ClientNetworkManager;
import org.example.Client.TradeMenu;
import org.example.Client.TradeScreen;
import org.example.Common.Item;
import org.example.Common.Player;
import org.example.Common.Request.TradeMessage;
import org.example.Common.Trade;
import org.example.Main;
import org.example.Server.models.*;

import java.util.ArrayList;
import java.util.List;


public class TradingController {
    private TradeScreen tradeScreen;
    private static ClientNetworkManager clientNetworkManager = Main.getMain().getNetworkManager();
    private static TradingController instance;
    private  List<Trade> trades = new ArrayList<>();
    private static final String tradeWithMoney = "trade\\s+-u\\s+.*\\s+-t\\s+(offer|request)\\s+-i\\s+.*-a\\s+\\d+\\s+-p\\s+\\d+";
    private static final String tradeWithItem = "trade\\s+-u\\s+.*\\s+-t\\s+(offer|request)\\s+-i\\s+.*-a\\s+\\d+\\s+-ti.*\\s+-ta\\s+.*";
    private static final GameMenuController gameMenuController = new GameMenuController();
    public void setTradeScreen(TradeScreen tradeScreen) {
        this.tradeScreen = tradeScreen;
    }

    public static TradingController getInstance() {
        if (instance == null) instance = new TradingController();
        return instance;
    }

    private Trade getTradeById(int id) {
        for (Trade trade : trades) {
            if (trade.id == id) return trade;
        }
        return null;
    }

    public Result trade(String input) {
        StringBuilder builder = new StringBuilder();
        int uIndex = -1, tIndex = -1, iIndex = -1, aIndex = -1, pIndex = -1, tiIndex = -1, taIndex = -1;
        String[] parts = input.split("\\s+");
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("-u")) uIndex = i;
            if (parts[i].equals("-t")) tIndex = i;
            if (parts[i].equals("-i")) iIndex = i;
            if (parts[i].equals("-a")) aIndex = i;
            if (parts[i].equals("-p")) pIndex = i;
            if (parts[i].equals("-ti")) tiIndex = i;
            if (parts[i].equals("-ta")) taIndex = i;
        }

        for (int i = uIndex + 1; i < tIndex; i++) {
            builder.append(parts[i]).append(" ");
        }
        String targetUsername = builder.toString().trim();
        builder.setLength(0);
        int amount;
        try {
            amount = Integer.parseInt(parts[aIndex + 1]);
        } catch (NumberFormatException e) {
            return new Result(false, "invalid amount");
        }
        builder.setLength(0);

        for (int i = iIndex + 1; i < aIndex; i++) {
            builder.append(parts[i]).append(" ");
        }
        String itemName = builder.toString().trim();
        builder.setLength(0);
        Item item = MyGame.getDatabase().getItem(itemName);

        if (item == null )
            return Result.error("item not found");
        Player targetPlayer = MyGame.getPlayerByUsername(targetUsername);
        if (targetPlayer == null) return new Result(false, "Trying to trade with imaginary friends again?");
      String type = input.contains("offer") ? "offer" : "request";
       if (MyGame.getCurrentPlayer().getItemQuantity(item) < amount && type.equals("offer")) {
           return new Result(false, "You're trying to give away more than you own. Generous… but impossible.");
       }


        if (input.contains("-p") && input.contains("-ti"))
            return new Result(false, "This isn’t an auction house. You’ve gotta choose: gold or goods!");

        if (input.matches(tradeWithMoney)) {
            int price;
            try {
                price = Integer.parseInt(parts[pIndex + 1]);
            } catch (NumberFormatException e) {
                return new Result(false, "invalid price");
            }
            if (MyGame.getCurrentPlayer().getGold() < price)
                return Result.error("You don't have enough gold.");
            trades.add(new Trade(MyGame.getCurrentPlayer().getUsername(), targetPlayer.getUsername(), item.getName(), amount, null, 0));
        } else if (input.matches(tradeWithItem)) {
            for (int i = tiIndex + 1; i < taIndex; i++) {
                builder.append(parts[i]).append(" ");
            }
            String targetItemName = builder.toString().trim();
            builder.setLength(0);
            Item targetItem = MyGame.getDatabase().getItem(targetItemName);
            int targetAmount;
            try {
                targetAmount = Integer.parseInt(parts[taIndex + 1]);
            } catch (NumberFormatException e) {
                return new Result(false, "invalid amount");
            }
            //trades.add(new Trade(MyGame.getCurrentPlayer(), targetPlayer, type, item, amount, null, targetItem, targetAmount));
        }
        targetPlayer.addNotification("You've got a new offer from "+ MyGame.getCurrentPlayer().getName() +"! Check your requests to respond.");
        return new Result(false, "trade request sent successfully");
    }
//
//    public Result respondToTrade(String input) {
//        boolean accepted = input.contains("-accept");
//        String[] parts = input.split("\\s+");
//        int iIndex = -1;
//        for (int i = 0; i < parts.length; i++) {
//            if (parts[i].equals("-i")) iIndex = i;
//        }
//        int tradeId;
//        try {
//            tradeId = Integer.parseInt(parts[iIndex + 1]);
//        } catch (NumberFormatException e) {
//            return new Result(false, "invalid tradeId");
//        }
//        Trade trade = getTradeById(tradeId);
//        if (trade == null) return new Result(false, "Trade with id " + tradeId + " not found");
//        trade.answered = true;
//        if (accepted) {
//            //trade.player.changeFriendshipXP(50, MyGame.getCurrentPlayer());
//            return Result.success("Deal struck! Hope you didn’t just get scammed.");
//        } else {
//            //trade.getSender().changeFriendshipXP(-30, MyGame.getCurrentPlayer());
//            return Result.success("You rejected the offer. Hopefully they won’t take it personally.");
//        }
//    }

//    public Result showTradeList() {
//        StringBuilder builder = new StringBuilder();
//        builder.append("========TRADES========");
//        if (trades.isEmpty()) return new Result(false, "No deals brewing yet. Why not start one?");
//        for (Trade trade : trades) {
//            if (!trade.answered &&
//                    (trade.getReceiver().equals(MyGame.getCurrentPlayer()) || trade.getSender().equals(MyGame.getCurrentPlayer()))) {
//                builder.append("\n").append("Id            : ").append(trade.getId()).append("\n");
//                builder.append("From          : ").append(trade.getSender().getName()).append("\n");
//                builder.append("To            : ").append(trade.getReceiver().getName()).append("\n");
//                builder.append("Type          : ").append(trade.getType()).append("\n");
//                builder.append("Item          : ").append(trade.getItem().getName()).append("\n");
//                builder.append("Amount        : ").append(trade.getAmount()).append("\n");
////                if (trade.getCost() != null) builder.append("Price         : ").append(trade.getCost()).append("\n");
////                else {
////                    builder.append("target item   : ").append(trade.getTargetItem().getName()).append("\n");
////                    builder.append("target amount : ").append(trade.getTargetAmount()).append("\n");
////                }
//                builder.append("======================");
//            }
//        }
//        return new Result(true, builder.toString());
//    }
//
//    public Result printTradeHistory() {
//        StringBuilder builder = new StringBuilder();
//        for (Trade trade : trades) {
//            builder.append("From          : ").append(trade.getSender().getName()).append("\n");
//            builder.append("To            : ").append(trade.getReceiver().getName()).append("\n");
//            builder.append("Type          : ").append(trade.getType()).append("\n");
//            builder.append("Item          : ").append(trade.getItem().getName()).append("\n");
//            builder.append("Amount        : ").append(trade.getAmount()).append("\n");
//            builder.append("Answered      : ").append(trade.isAnswered()).append("\n");
////            if (trade.getCost() != null) builder.append("Price         : ").append(trade.getCost()).append("\n");
////            else {
////                builder.append("target item   : ").append(trade.getTargetItem().getName()).append("\n");
////                builder.append("target amount : ").append(trade.getTargetAmount()).append("\n");
////            }
//            builder.append("======================").append("\n");
//        }
//        return Result.success(builder.toString());
//    }

    public void sendTradeOffer(TradeMessage message) {
        //create trade message object
        TradeMessage msg = new TradeMessage();
        msg.fromPlayer = message.fromPlayer;
        msg.toPlayer = message.toPlayer;
        msg.type = TradeMessage.MessageType.REQUEST;
        Trade trade = new Trade(msg.fromPlayer, msg.toPlayer, msg.trade.item, 1, msg.trade.targetItem, 1);
        msg.trade = trade;

        clientNetworkManager.getClient().sendTCP(msg); //send offer message
    }

    public void acceptTradeOffer(TradeMessage message) {
        //create trade accept message
        TradeMessage msg = new TradeMessage();
        msg.fromPlayer = message.fromPlayer;
        msg.toPlayer = message.toPlayer;
        msg.type = TradeMessage.MessageType.ACCEPT;
        msg.trade = message.trade;
        clientNetworkManager.getClient().sendTCP(msg);
        MyGame.getCurrentPlayer().addTrade(msg.fromPlayer, msg.trade);
        Item selectedOfferItem = gameMenuController.getItemByName(msg.trade.item);
        Item selectedRequestItem = gameMenuController.getItemByName(msg.trade.targetItem);
        updateInventory(selectedRequestItem, msg.trade.targetAmount, selectedOfferItem, msg.trade.amount);

    }

    public void rejectTrade(String fromPlayer, String toPlayer) {
        //create rejection message :(
        TradeMessage msg = new TradeMessage();
        msg.fromPlayer = fromPlayer;
        msg.toPlayer = toPlayer;
        msg.type = TradeMessage.MessageType.DECLINE;
        clientNetworkManager.getClient().sendTCP(msg);
    }

    public void completeTrade(TradeMessage msg) {
        Trade trade = new Trade(msg.fromPlayer, msg.toPlayer, msg.trade.item, 1, msg.trade.targetItem, 1);
        MyGame.getCurrentPlayer().addTrade(msg.toPlayer, trade);
        Item selectedOfferItem = gameMenuController.getItemByName(trade.item);
        Item selectedRequestItem = gameMenuController.getItemByName(trade.targetItem);
        updateInventory(selectedOfferItem, trade.amount, selectedRequestItem, trade.targetAmount);
    }

    private void updateInventory(Item itemToRemove, int itemToRemoveAmount, Item itemToAdd, int itemToAddAmount) {
        if(itemToAdd == null || itemToRemove == null) return; //TODO implement
        MyGame.getCurrentPlayer().getBackPack().addToInventory(itemToAdd, itemToAddAmount);
        MyGame.getCurrentPlayer().getBackPack().removeFromInventory(itemToRemove, itemToRemoveAmount);
    }
    public void updateOfferSlot(Trade trade) {
        Item item = gameMenuController.getItemByName(trade.item);
        Image itemImage = new Image(item.getTexture());
        tradeScreen.updateSlotFromText(item.getName(), itemImage, true);
    }

    public void updateRequestSlot(Trade trade) {
        Item item = gameMenuController.getItemByName(trade.targetItem);
        Image itemImage = new Image(item.getTexture());
        tradeScreen.updateSlotFromText(item.getName(), itemImage, false);
    }

}
