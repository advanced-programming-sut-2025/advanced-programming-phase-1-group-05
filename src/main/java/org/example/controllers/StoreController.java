package org.example.controllers;

import org.example.models.*;

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
            return Result.error("Nice try, but the valley's still one solar panel away from online shopping.");
        }
        Player player = Game.getCurrentPlayer();
        Product product = store.getProduct(productName);
        if (product == null) {
            return Result.error("That item doesn't exist. But hey, points for creativity!");
        }

        if (player.getGold() < product.getPrice()*count){
            return Result.error("You reach for your wallet... and it echoes. Try again when it stops crying.");
        }

        if (!store.contains(product)) {
            return Result.error("This store doesn't have that product. Maybe try shopping elsewhere.");
        }

        if(product.getRemainingForToday() < count) {
            return Result.error("Can't purchase any more of that. come back tomorrow!");
        }
        return null;
    }

    public Result sell(String productName, int count) {

        Player currentPlayer = Game.getCurrentPlayer();
        Item item = Game.getDatabase().getItem(productName);
        if (item == null) return Result.error("try selling something that exists!");
        if (currentPlayer.getItemQuantity(item) == 0 )
            return Result.error("You present your empty hands with confidence. Sadly, buyers prefer actual stuff");
        if (currentPlayer.getItemQuantity(item) < count) {
            return Result.error("You can't sell what you don't have. Unless you're secretly a magician");
        }

        if (!currentPlayer.getFarm().getShippingBin().isNear(currentPlayer.getX(), currentPlayer.getY()))
            return Result.error("You can't just toss things into air and hope for a sale. Find a shipping bin first.");
        Game.soldItems.put(currentPlayer, item);
        return Result.success("You will recieve the gold tomorrow morning!");
    }




}