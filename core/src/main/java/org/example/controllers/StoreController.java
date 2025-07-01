package org.example.controllers;

import org.example.models.*;
import org.example.models.Building.AnimalHouse;
import org.example.models.Enums.*;
import org.example.models.Tool.Tool;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class StoreController {

    private Store getCurrentStore() {
        Player player = MyGame.getCurrentPlayer();
        for (Store store : MyGame.getDatabase().getStores()){
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
            if (product.isAvailable(store)) {
                output.append(product.getName()).append(" ").append(product.getPrice()).append("\n");
            }
        }
        return Result.success(output.toString());
    }
    public Result purchase(Matcher m) {
        String productName = m.group("productName");
        int count = 1;
        if (m.group("count") != null)
            count = Integer.parseInt(m.group("count"));
        Store store = getCurrentStore();
        if (store == null) {
            return Result.error("Nice try, but the valley's still one solar panel away from online shopping.");
        }
        if (!store.isOpen(GameManager.getCurrentHour()))
            return Result.error("store not open right now.");
        Player player = MyGame.getCurrentPlayer();
        Product product = store.getProduct(productName);
        if (MyGame.getDatabase().getItem(productName) == null)
            return Result.error("That item doesn't exist. But hey, points for creativity!");
        if (product == null) {
            return Result.error("This store doesn't have that product. Maybe try shopping elsewhere.");
        }

        if (player.getGold() < product.getPrice()*count){
            return Result.error("You reach for your wallet... and it echoes. Try again when it stops crying.");
        }
        if (!product. isInSeason(store)) {
            return Result.error("This product isn't available in " + GameManager.getSeason() + " in this store.");
        }
        if(product.getRemainingForToday() < count && product.getLimit() != -1) {
            return Result.error("Can't purchase any more of that. come back tomorrow!");
        }


        player.addGold(-product.getPrice()*count);

        //change backpack capacity
        if(productName.contains("Pack")) {
            if(productName.equals("Large Pack")) {
                MyGame.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Big);
            } else if(productName.equals("Deluxe Pack")) {
                MyGame.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Deluxe);
            }
            //change trash can
        } else if(productName.contains("Trash Can")) {
            if(productName.equals("Copper Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Brass);
            } else if(productName.equals("Steel Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iron);
            } else if(productName.equals("Gold Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Gold);
            } else if(productName.equals("Iridium Trash Can")) {
                MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iridium);
            }
            //add craft recipes to learnt recipes
        } else if(productName.contains("Recipe")) {
            if (productName.equals("Dehydrator Recipe")) {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Dehydrator);
            } else if (productName.equals("Grass Starter Recipe")) {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.GrassStarter);
            } else if (productName.equals("Fish Smoker Recipe")) {
                MyGame.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.FishSmoker);
            } else {
                String name = productName.replace("Recipe", "");
                CookingRecipeType type = CookingRecipeType.fromString(name);
                MyGame.getCurrentPlayer().getBackPack().addLearntCookingRecipe(type);
            }

        } else {
            player.getBackPack().addToInventory(product, count);
        }
        product.addSold(count);
        return Result.success("Purchased " + productName + " successfully!");
    }

    public Result sell(Matcher m) {
        int count;

        String productName = m.group("productName");
        Player currentPlayer = MyGame.getCurrentPlayer();
        Item item = currentPlayer.getBackPack().getFromInventory(productName);
        if (item instanceof Tool<?>) {
            return Result.error("Nice try, but the shipping bin has standards. Tools not accepted.");
        }
        if (MyGame.getDatabase().getItem(productName) == null) return Result.error("try selling something that exists!");
        if (currentPlayer.getItemQuantity(item) == 0 )
            return Result.error("You present your empty hands with confidence. Sadly, buyers prefer actual stuff");
        if (m.group("count") != null) count = Integer.parseInt(m.group("count"));
        else count = currentPlayer.getItemQuantity(item);
        if (currentPlayer.getItemQuantity(item) < count) {
            return Result.error("You can't sell what you don't have. Unless you're secretly a magician");
        }

        if (!currentPlayer.getFarm().getShippingBin().isNear(currentPlayer.getX(), currentPlayer.getY()))
            return Result.error("You can't just toss things into air and hope for a sale. Find a shipping bin first.");

        MyGame.soldItems.put(currentPlayer, item);
        return Result.success("You will receive the gold tomorrow morning!");
    }

    public Result buyAnimal(Matcher m) {
        Store store = getCurrentStore();
        if (store == null || !store.getStoreName().equalsIgnoreCase("marnie's ranch")) {
            return Result.error("You can only do this in the carpenter's shop");
        }

        Product product = store.getProduct(m.group("animalType"));
        if (product == null) {
            return Result.error("Can't find animal type " + m.group("animalType"));
        }
        Player player = MyGame.getCurrentPlayer();
        if (player.getGold() < product.getPrice()) {
            return Result.error("You do not have enough money to buy this animal!");
        }
        EnclosureType enclosureType = EnclosureType.fromString(product.getBuildingType().toString());

        if (enclosureType == null) {
            return Result.error("This shouldn't happen");
        }
        AnimalHouse animalHouse = player.hasThisEnclosureType(product.getBuildingType());
        if (animalHouse == null) {
            return Result.error("You don't have an empty " + enclosureType.toString().toLowerCase());
        }
        AnimalType type = AnimalType.fromString(m.group("animalType"));
        animalHouse.addAnimal(new Animal(m.group("animalName"), type, player));
        player.addGold(-product.getPrice());
        return Result.success("animal bought successfully!");
    }

    public Result buildAnimalHouse(Matcher m) {
        Store store = getCurrentStore();
        if (store == null || !store.getStoreName().equals("Carpenter's shop")) {
            return Result.error("You can only do this in the carpenter's shop.");
        }

        Product product = store.getProduct(m.group("buildingName"));
        EnclosureType type;
        if (product == null)
            return Result.error("invalid enclosure type");
        if (product.getName().contains("Coop")) {
            type = EnclosureType.COOP;
        }
        else if (product.getName().contains("Barn")) {
            type = EnclosureType.BARN;
        }
        else   return Result.error("You can only build a coop or a barn.");
        int x = Integer.parseInt(m.group("x")), y = Integer.parseInt(m.group("y"));

        Player player = MyGame.getCurrentPlayer();
        for (int i = x; i < type.getRows() + x; i++) {
            for (int j = y; j < type.getColumns() + y; j++) {
                GameTile tile = MyGame.getGameMap().getTile(i, j);
                if (tile == null || !tile.getTileType().equals(TileType.Soil)) {
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
                GameTile tile = MyGame.getGameMap().getTile(i, j);
                tile.setTileType(TileType.Building);
                // اینجا باید چک شه که نباشه چیزی ولی خب
                tile.setItemOnTile(null);
                tile.setBuilding(animalHouse.getType());
            }
        }
        return Result.success(product.getName() + " built successfully!");

    }



    private boolean canAfford(Product product) {
        for (Map.Entry<String, Integer> cost : product.getCosts().entrySet()) {
            Item item = MyGame.getDatabase().getItem(cost.getKey());
            int quantity = cost.getValue();
            if (MyGame.getCurrentPlayer().getItemQuantity(item) < quantity) {
                return false;
            }
        }
        for (Map.Entry<String, Integer> cost : product.getCosts().entrySet()) {
            Item item = MyGame.getDatabase().getItem(cost.getKey());
            int quantity = cost.getValue();
            Player currentPlayer = MyGame.getCurrentPlayer();
            currentPlayer.getBackPack().removeFromInventory(item, quantity);
        }
        return true;
    }

    //upgrade tool
    public Result upgradeTool(String name) {

        Store store = getCurrentStore();
        if (store == null || !store.getStoreName().equals("Blacksmith")) {
            return Result.error("You can only upgrade tools in the blacksmith.");
        }
        HashMap<Item, Integer> items = MyGame.getCurrentPlayer().getBackPack().getInventory();
        for (Item item : items.keySet()) {
            if (item.getName().equalsIgnoreCase(name)) {
                if (item instanceof Tool) {
                    ItemLevel levelBeforeUpgrading = (ItemLevel) ((Tool) item).getLevel();
                    if ( levelBeforeUpgrading == ItemLevel.Iridium)
                        return Result.error("Looks like this tool’s already maxed out. Even Clint couldn’t make it better!");
                    ItemLevel level = levelBeforeUpgrading.upgradeLevel();
                    String levelName = level.toString();
                    if (level == ItemLevel.Brass ) levelName = "Copper";
                    else if (level == ItemLevel.Iron) levelName = "Steel";
                    Product product = store.getProduct(levelName + " tool");
                    if (product == null) return Result.success("can't upgrade tool");
                    int price  = store.getProduct(levelName + " tool").getPrice();
                    Player player = MyGame.getCurrentPlayer();
                    if (price > player.getGold())
                        return Result.error("You can't afford to upgrade this tool!");
                    MyGame.getCurrentPlayer().addGold(-price);
                    ((Tool) item).upgradeLevel();
                    return new Result(true, item.getName() + " upgraded to level " + ((Tool) item).getLevel());
                } else {
                    return new Result(true, "Selected item is not a tool");
                }
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }

}
