package org.example.models;

import org.example.models.Enums.AnimalType;
import org.example.models.Enums.EnclosureType;
import org.example.models.Enums.ItemLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Animal {
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


    public void produce() {
        if (wasFed) {
            Random random = new Random();
            int index = 0;
            if (friendshipPoints > 100) {
                int chance = (int) ((friendshipPoints + (150 * (random.nextDouble(1)+0.5))) / 1500);
                if (random.nextInt(100) > chance) index = 1;
            }
            Item item = products.get(index);
            Product product = new Product(item.getName(), item.getPrice(), 0, null, null, null);
            unCollectedProducts.add(product);
            int levelValue = (int) ((friendshipPoints/1000) * (0.5 + 0.5*random.nextDouble(1)));
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

    public List<Product> getUnCollectedProducts() {
        return unCollectedProducts;
    }
}