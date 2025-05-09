package org.example.controllers;

import org.example.models.*;
import org.example.models.Building.AnimalHouse;
import org.example.models.Enums.AnimalHouseLevel;
import org.example.models.Enums.EnclosureType;
import org.example.models.Enums.TileType;
import org.example.models.Tool.Tool;

import java.util.Map;
import java.util.regex.Matcher;

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
        if (!store.isOpen(GameManager.getCurrentHour()))
            return Result.error("store not open right now.");
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
        if (!store.isOpen(GameManager.getCurrentHour()))
            return Result.error("store not open right now.");
        output.append("Available products:\n");
        for (Product product : store.getProducts()) {
            if (product.isAvailable()) {
                output.append(product.getName()).append(" ").append(product.getPrice()).append("\n");
            }
        }
        return Result.success(output.toString());
    }
    public Result purchase(Matcher m) {
        String productName = m.group("productName");
        int count = Integer.parseInt(m.group("count"));

        Store store = getCurrentStore();
        if (store == null) {
            return Result.error("Nice try, but the valley's still one solar panel away from online shopping.");
        }
        if (!store.isOpen(GameManager.getCurrentHour()))
            return Result.error("store not open right now.");
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
        if (!product. isInSeason(store)) {
            return Result.error("This product isn't available in " + GameManager.getSeason() + " in this store.");
        }
        if(product.getRemainingForToday() < count) {
            return Result.error("Can't purchase any more of that. come back tomorrow!");
        }

        player.addGold(-product.getPrice()*count);
        player.getBackPack().addToInventory(product, count);
        return Result.success("Purchased " + productName + " successfully!");
    }

    public Result sell(Matcher m) {
        int count = Integer.parseInt(m.group("count"));
        String productName = m.group("productName");
        Player currentPlayer = Game.getCurrentPlayer();
        Item item = currentPlayer.getBackPack().getFromInventory(productName);
        if (Game.getDatabase().getItem(productName) == null) return Result.error("try selling something that exists!");
        if (currentPlayer.getItemQuantity(item) == 0 )
            return Result.error("You present your empty hands with confidence. Sadly, buyers prefer actual stuff");
        if (currentPlayer.getItemQuantity(item) < count) {
            return Result.error("You can't sell what you don't have. Unless you're secretly a magician");
        }

        if (!currentPlayer.getFarm().getShippingBin().isNear(currentPlayer.getX(), currentPlayer.getY()))
            return Result.error("You can't just toss things into air and hope for a sale. Find a shipping bin first.");
        if (item instanceof Tool<?>) {
            return Result.error("Nice try, but the shipping bin has standards. Tools not accepted.");
        }
        Game.soldItems.put(currentPlayer, item);
        return Result.success("You will receive the gold tomorrow morning!");
    }

    public Result buildAnimalHouse(Matcher m) {
        Store store = getCurrentStore();
        if (store == null || !store.getStoreName().equals("Carpenter's shop")) {
            return Result.error("You can only do this in the carpenter's shop.");
        }

        Product product = store.getProduct(m.group("buildingName"));
        EnclosureType type;
        if (product.getName().contains("Coop")) {
            type = EnclosureType.COOP;
        }
        else if (product.getName().contains("Barn")) {
            type = EnclosureType.BARN;
        }
        else   return Result.error("You can only build a coop or a barn.");
        int x = Integer.parseInt(m.group("x")), y = Integer.parseInt(m.group("y"));

        Player player = Game.getCurrentPlayer();
        for (int i = x; i < type.getRows() + x; i++) {
            for (int j = y; j < type.getColumns() + y; j++) {
                GameTile tile = Game.getGameMap().getTile(i, j);
                if (tile == null || !tile.getTileType().equals(TileType.Flat)) {
                    return Result.error("You can’t build here. The area must be completely flat.");
                }
                if (!player.getFarm().containsTile(i , j))
                    return Result.error("Nice try, but that patch of land isn’t yours. No trespassing… or building!");
            }
        }

        if (!canAfford(product)) {
            return Result.error("Looks like your gold took one look at the blueprint and noped out. You can’t afford to build this right now!");
        }
        player.addGold(-product.getPrice());

        AnimalHouseLevel level;
        if (product.getName().contains("Big")) level = AnimalHouseLevel.Big;
        else if (product.getName().contains("Deluxe")) level = AnimalHouseLevel.Deluxe;
        else level = AnimalHouseLevel.Small;
        AnimalHouse animalHouse = new AnimalHouse(type, level);
        player.addAnimalHouse(animalHouse);

        for (int i = x; i < type.getRows() + x; i++) {
            for (int j = y; j < type.getColumns() + y; j++) {
                GameTile tile = Game.getGameMap().getTile(i, j);
                tile.setTileType(TileType.Building);
                tile.setBuilding(animalHouse.getType());
            }
        }
        return Result.success(type + " built successfully!");

    }



    private boolean canAfford(Product product) {
        for (Map.Entry<Item, Integer> cost : product.getCosts().entrySet()) {
            Item item = cost.getKey();
            int quantity = cost.getValue();
            if (Game.getCurrentPlayer().getItemQuantity(item) < quantity) {
                return false;
            }
        }
        for (Map.Entry<Item, Integer> cost : product.getCosts().entrySet()) {
            Item item = cost.getKey();
            int quantity = cost.getValue();
            Player currentPlayer = Game.getCurrentPlayer();
            currentPlayer.getBackPack().getInventory().remove(item, quantity);
        }
        return true;
    }

}