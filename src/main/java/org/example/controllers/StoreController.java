package org.example.controllers;

import org.example.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreController {

    private Store getCurrentStore() {
        Player player = Game.getCurrentPlayer();
        for (Store store : Game.getDatabase().getStores()){
            if (store.isInside(player.getX(), player.getY())){
                return store;
            }
        }
        return null;
    }

    public Result showAllProducts() {
        StringBuilder output = new StringBuilder();
        Store store = getCurrentStore();
        if (store == null)
            return Result.error("No store, no shelves, no products.");
        output.append("All products: \n");
        for (Product product : store.getProducts()) {
                output.append(product.getName()).append(" ").append(product.getPrice()).append("\n");
        }
        return Result.success(output.toString());
    }

    public Result showAvailableProducts() {
        StringBuilder output = new StringBuilder();
        Store store = getCurrentStore();
        if (store == null)
            return Result.error("No store, no shelves, no products.");
        output.append("Available products:\n");
        for (Product product : store.getProducts()) {
            if (product.isAvailable()) {
                output.append(product.getName()).append(" ").append(product.getPrice()).append("\n");
            }
        }
        return Result.success(output.toString());
    }
    public Result purchase(String productName, int count) {
        Store store = getCurrentStore();
        if (store == null) {
            return Result.error(" ");
        }
        Product product = store.getProduct(productName);

        if (product == null) {
            return Result.error(" ");
        }
        //sell item
        if(product.getLimit() < count) {
            //can't purchase
        } else if (Game.getCurrentPlayer().getGold() < product.getPrice()){
            //not enough gold
        }
        return null;
    }
//
//    //cheat code for increasing ??
//    public Result addDollars(int amount) {
//
//        return null;
//    }

    public Result sell(String productName, int count) {
        Store store = getCurrentStore();
        if (store == null) {
            return Result.error(" ");
        }
        Player currentPlayer = Game.getCurrentPlayer();
        Item item = Game.getDatabase().getItem(productName);

        Game.soldItems.put(currentPlayer, item);
        return Result.success(" ");
    }




}