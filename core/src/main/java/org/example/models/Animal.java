package org.example.models;

import org.example.models.Enums.AnimalType;
import org.example.models.Enums.EnclosureType;
import org.example.models.Enums.ItemLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Animal implements Item{
    private String name;
    private EnclosureType enclosureType;
    private List<Item> products = new ArrayList<>();
    private List<Product> unCollectedProducts = new ArrayList<>();
    private boolean wasFed = false, petToday  = false, isOut = false;
    int friendshipPoints = 0;
    private AnimalType type;
    private int x, y;


    public Animal(String name, AnimalType type, Player player) {
        this.name = name;
        this.type = type;
        initializeAnimal();
        setCoordinates(player);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }
    private void setCoordinates(Player player) {
        Farm farm = player.getFarm();
        int startX = farm.getStartX(), startY = farm.getStartY(), endX = farm.getEndX(), endY = farm.getEndY();
        Random random = new Random();

        while (true) {
            int x = random.nextInt(endX - startX + 1) + startX;
            int y = random.nextInt(endY - startY + 1) + startY;
            GameTile tile = GameMap.getTile(x, y);

            if (!tile.isOccupied()) {
                this.x = x;
                this.y = y;
                tile.setItemOnTile(this);
                break;
            }
        }
    }

    public AnimalType getType() {
        return type;
    }

    public void produce() {
        if (wasFed) {
            Random random = new Random();
            int index = 0;
            if (friendshipPoints > 100) {
                int chance = (int) (friendshipPoints + (150 * (0.5 + random.nextDouble())) / 1500);
                if (random.nextInt(100) > chance) index = 1;
            }
            Item item = products.get(index);
            Product product = new Product(item.getName(), item.getPrice(), 0, null, List.of(), Map.of());
            unCollectedProducts.add(product);
            int levelValue = (int) ((friendshipPoints/1000) * (0.5 + random.nextDouble()));
            ItemLevel level;
            if (levelValue < 0.5) level = ItemLevel.Normal;
            else if (levelValue >= 05 && levelValue < 0.7) level = ItemLevel.Iron;
            else if (levelValue >= 0.7 && levelValue < 0.9) level = ItemLevel.Gold;
            else level = ItemLevel.Iridium;
            product.setItemLevel(level);
        }
    }
    public String getName() {
        return name;
    }

    @Override
    public int getPrice() {
        return 0;
    }


    private void initializeAnimal() {
        switch (type) {
            case COW : {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Milk"));
                products.add(Game.getDatabase().getItem("Large milk"));
                break;
            }
            case GOAT : {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Goat Milk"));
                products.add(Game.getDatabase().getItem("Large goat milk"));
                break;
            }
            case PIG : {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Truffle"));
                break;
            }
            case DUCK : {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Duck feather"));
                products.add(Game.getDatabase().getItem("Duck egg"));
                break;
            }
            case RABBIT : {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Wool"));
                products.add(Game.getDatabase().getItem("Rabbit's foot"));
                break;
            }
            case SHEEP : {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Wool"));
                break;
            }
            case CHICKEN : {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Egg"));
                products.add(Game.getDatabase().getItem("Large egg"));
                break;
            }

        }
    }

    public void adjustFriendshipPoints (int amount) {
        friendshipPoints = Math.min(1000, friendshipPoints + amount);
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }

    public void setFriendshipPoints(int amount) {
        friendshipPoints = Math.min(1000, amount);
    }

    public boolean wasFed() {
        return  wasFed;
    }
    public boolean wasPetToday() {
        return petToday;
    }
    public void setPetToday(boolean petToday) {
        this.petToday = petToday;
    }
    public List<Product> getUnCollectedProducts() {
        return unCollectedProducts;
    }
    public void setFeedingStatus(boolean wasFed) {
        this.wasFed = wasFed;
    }
    public void shepherd () {
        isOut = !isOut;
    }

    public boolean isOut() {
        return isOut;
    }

}
