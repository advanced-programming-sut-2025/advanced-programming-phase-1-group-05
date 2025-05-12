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
    private boolean wasFed = false;
    int friendshipPoints = 0;
    private AnimalType type;

    public Animal(String name, AnimalType type) {
        this.name = name;
        this.type = type;
        initializeAnimal();
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
            Product product = new Product(item.getName(), item.getPrice(), 0, null, null, null);
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

    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {

    }

    @Override
    public Map.Entry<Integer, Integer> getCoordinates() {
        return null;
    }

    private void initializeAnimal() {
        switch (type) {
            case COW -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Milk"));
                products.add(Game.getDatabase().getItem("Large milk"));
            }
            case GOAT -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Goat Milk"));
                products.add(Game.getDatabase().getItem("Large goat milk"));
            }
            case PIG -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Truffle"));
            }
            case DUCK -> {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Duck feather"));
                products.add(Game.getDatabase().getItem("Duck egg"));
            }
            case RABBIT -> {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Wool"));
                products.add(Game.getDatabase().getItem("Rabbit's foot"));
            }
            case SHEEP -> {
                enclosureType = EnclosureType.BARN;
                products.add(Game.getDatabase().getItem("Wool"));
            }
            case CHICKEN -> {
                enclosureType = EnclosureType.COOP;
                products.add(Game.getDatabase().getItem("Egg"));
                products.add(Game.getDatabase().getItem("Large egg"));
            }

        }
    }

    public void adjustFriendshipPoints (int amount) {
        friendshipPoints += amount;
    }

    public int getFriendshipPoints() {
        return friendshipPoints;
    }

    public void setFriendshipPoints(int amount) {
        friendshipPoints = Math.min(1000, amount);
    }
    public List<Product> getUnCollectedProducts() {
        return unCollectedProducts;
    }
    public void setFeedingStatus(boolean wasFed) {
        this.wasFed = wasFed;
    }
}