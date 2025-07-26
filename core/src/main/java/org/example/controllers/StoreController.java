package org.example.controllers;

import com.badlogic.gdx.Game;
import org.example.models.*;
import org.example.models.Building.AnimalHouse;
import org.example.models.Enums.*;
import org.example.models.Tool.FishingPole;
import org.example.models.Tool.Tool;
import org.example.views.GameScreen;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

public class StoreController {
    private static StoreController instance;



    public static StoreController getInstance() {
        if (instance == null) instance = new StoreController();
        return instance;
    }
    private Store getCurrentStore() {
        Player player = MyGame.getCurrentPlayer();
        for (Store store : MyGame.getDatabase().getStores()){
            if (store.isInside(player.getXX(), player.getYY())){
                return store;
            }
        }
        return null;
    }


    public Result purchase(Map<Product, Integer> products, GameScreen screen) {

        Player player = getPlayer(products);


        //change backpack capacity
        for (Map.Entry<Product, Integer> entry : products.entrySet()){
            String productName = entry.getKey().getName();
            FishingPoleType poleType = FishingPoleType.fromString(productName);
            if (poleType != null) {
                FishingPole pole = new FishingPole();
                pole.setFishingPoleType(poleType);
                player.getBackPack().addToInventory(pole, entry.getValue());
            }
            else if (productName.contains("Pack")) {
                if (productName.equals("Large Pack")) {
                    MyGame.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Big);
                } else if (productName.equals("Deluxe Pack")) {
                    MyGame.getCurrentPlayer().getBackPack().setBackPackType(BackPackType.Deluxe);
                }
                //change trash can
            } else if (productName.contains("Trash Can")) {
                if (productName.equals("Copper Trash Can")) {
                    MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Brass);
                } else if (productName.equals("Steel Trash Can")) {
                    MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iron);
                } else if (productName.equals("Gold Trash Can")) {
                    MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Gold);
                } else if (productName.equals("Iridium Trash Can")) {
                    MyGame.getCurrentPlayer().getTrashCan().setLevel(ItemLevel.Iridium);
                }
                //add craft recipes to learnt recipes
            } else if (productName.contains("Recipe")) {
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
                player.getBackPack().addToInventory(entry.getKey(), entry.getValue());
            }
            entry.getKey().addSold(entry.getValue());
        }
        return Result.success("bought successfully!");
    }

    private static Player getPlayer(Map<Product, Integer> products) {
        Player player = MyGame.getCurrentPlayer();
//        if (!product. isInSeason(store)) {
//            return Result.error("This product isn't available in " + GameManager.getSeason() + " in this store.");
//        }
//        if(product.getRemainingForToday() < count && product.getLimit() != -1) {
//            return Result.error("Can't purchase any more of that. come back tomorrow!");
//        }

        int price = 0;
        for (Map.Entry<Product, Integer> entry : products.entrySet()) {
            price += entry.getValue() * entry.getKey().getPrice();
        }


        player.addGold(-price);
        return player;
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
//
//        if (!currentPlayer.getFarm().getShippingBin().isNear(currentPlayer.getX(), currentPlayer.getY()))
//            return Result.error("You can't just toss things into air and hope for a sale. Find a shipping bin first.");

        MyGame.soldItems.put(currentPlayer, item);
        return Result.success("You will receive the gold tomorrow morning!");
    }

    public Result buyAnimal(AnimalType type,Product product, GameScreen game, Animal animal) {

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
        animalHouse.addAnimal(animal);
        player.addGold(-product.getPrice());
        game.addAnimalActor(new AnimalActor(animal));
        return Result.success("animal bought successfully!");
    }

    public Result buildAnimalHouse(EnclosureType type, AnimalHouseLevel level, float x, float y) {
        Player player = MyGame.getCurrentPlayer();
        for (int i = (int) x; i < type.getRows() + x; i++) {
            for (int j = (int) y; j < type.getColumns() + y; j++) {
                GameTile tile = MyGame.getGameMap().getTile(i, j);
                if (tile == null || !tile.getTileType().equals(TileType.Soil)) {
                    return Result.error("You can’t build here. The area must be completely flat.");
                }
                if (!player.getFarm().isInFarm(i , j))
                    return Result.error("Nice try, but that patch of land isn’t yours. No trespassing… or building!");
            }
        }
//        AnimalHouse animalHouse = new AnimalHouse(type, level, x, y);
//        player.addAnimalHouse(animalHouse);
//
//        for (int i = x; i < type.getRows() + x; i++) {
//            for (int j = y; j < type.getColumns() + y; j++) {
//                GameTile tile = MyGame.getGameMap().getTile(i, j);
//                tile.setTileType(TileType.House);
//                // اینجا باید چک شه که نباشه چیزی ولی خب
//                tile.setItemOnTile(null);
//                tile.setBuilding(animalHouse.getType());
//            }
//        }
        return Result.success(type + " built successfully!");

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
