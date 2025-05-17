package org.example.models.Enums;

import org.example.models.Game;
import org.example.models.Item;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public enum CookingRecipeType implements Material, Item {
    FriedEgg("Fried Egg", () -> Map.of(Game.getDatabase().getItem("Egg"), 1), 50, "", "Starter", 35),
    BakedFish("Baked Fish", () -> Map.of(Fish.Sardine, 1, Fish.Salmon, 1, CropType.Wheat, 1), 75, "", "Starter", 100),
    Salad("Salad", () -> Map.of(ForagingCrop.Leek, 1, Game.getDatabase().getItem("Dandelion"), 1), 113, "", "Starter", 110),
    Omelet("Omelet", () -> Map.of(Game.getDatabase().getItem("Egg"), 1, Game.getDatabase().getItem("Milk"), 1), 100, "", "Stardrop Saloon", 125),
    PumpkinPie("Pumpkin Pie", () -> Map.of(
            CropType.Pumpkin, 1,
            Game.getDatabase().getItem("Wheat Flour"), 1,
            Game.getDatabase().getItem("Milk"), 1,
            Game.getDatabase().getItem("Sugar"), 1), 225, "", "Stardrop Saloon", 385),

    Spaghetti("Spaghetti", () -> Map.of(Game.getDatabase().getItem("Wheat Flour"), 1, CropType.Tomato, 1), 75, "", "Stardrop Saloon", 120),
    Pizza("Pizza", () -> Map.of(Game.getDatabase().getItem("Wheat Flour"), 1, CropType.Tomato, 1, Game.getDatabase().getItem("Cheese"), 1), 150, "", "Stardrop Saloon", 300),
    Tortilla("Tortilla", () -> Map.of(CropType.Corn, 1), 50, "", "Stardrop Saloon", 50),
    MakiRoll("Maki Roll", () -> Map.of(Fish.Sardine, 1, Game.getDatabase().getItem("Rice"), 1, Game.getDatabase().getItem("Fiber"), 1), 100, "", "Stardrop Saloon", 220),
    TripleShotEspresso("Triple Shot Espresso", () -> Map.of(Game.getDatabase().getItem("Coffee"), 3), 200, "Max Energy + 100 (5 hours)", "Stardrop Saloon", 450),
    Cookie("Cookie", () -> Map.of(Game.getDatabase().getItem("Wheat Flour"), 1, Game.getDatabase().getItem("Sugar"), 1, Game.getDatabase().getItem("Egg"), 1), 90, "", "Stardrop Saloon", 140),
    HashBrowns("Hash Browns", () -> Map.of(CropType.Potato, 1, Game.getDatabase().getItem("Oil"), 1), 90, "Farming (5 hours)", "Stardrop Saloon", 120),
    Pancakes("Pancakes", () -> Map.of(Game.getDatabase().getItem("Wheat Flour"), 1, Game.getDatabase().getItem("Egg"), 1), 90, "Foraging (11 hours)", "Stardrop Saloon", 80),
    FruitSalad("Fruit Salad", () -> Map.of(CropType.Blueberry, 1, CropType.Melon, 1, Game.getDatabase().getItem("Apricot"), 1), 263, "", "Stardrop Saloon", 450),
    RedPlate("Red Plate", () -> Map.of(CropType.RedCabbage, 1, CropType.Radish, 1), 240, "Max Energy + 50 (3 hours)", "Stardrop Saloon", 400),
    Bread("Bread", () -> Map.of(Game.getDatabase().getItem("Wheat Flour"), 1), 50, "", "Stardrop Saloon", 60),
    SalmonDinner("Salmon Dinner", () -> Map.of(Fish.Salmon, 1, CropType.Amaranth, 1, CropType.Kale, 1), 125, "", "Leah Reward", 300),
    VegetableMedley("Vegetable Medley", () -> Map.of(CropType.Tomato, 1, CropType.Beet, 1), 165, "", "Foraging Level 2", 120),
    FarmersLunch("Farmer's Lunch", () -> Map.of(CropType.HotPepper, 1, CropType.Parsnip, 1), 200, "Farming (5 hours)", "Farming level 1", 150),
    SurvivalBurger("Survival Burger", () -> Map.of(CropType.HotPepper, 1, CropType.Carrot, 1, CropType.Eggplant, 1), 125, "Foraging (5 hours)", "Foraging level 3", 180),
    DishOTheSea("Dish O' the Sea", () -> Map.of(Fish.Sardine, 2, CropType.HotPepper, 1), 150, "Fishing (5 hours)", "Fishing level 2", 220),
    SeaformPudding("Seaform Pudding", () -> Map.of(Fish.Flounder, 1, Fish.MidnightCarp, 1), 175, "Fishing (10 hours)", "Fishing level 3", 300),
    MinersTreat("Miner's Treat", () -> Map.of(CropType.Carrot, 1, Game.getDatabase().getItem("Egg"), 1, Game.getDatabase().getItem("Milk"), 1), 125, "Mining (5 hours)", "Mining level 1", 100);

    private final String name;
    private final Supplier<Map<Item, Integer>> ingredientsSupplier;
    private final int energy;
    private final String buff;
    private final String source;
    private final int price;

    CookingRecipeType(String name, Supplier<Map<Item, Integer>> ingredientsSupplier, int energy, String buff, String source, int price) {
        this.name = name;
        this.ingredientsSupplier = ingredientsSupplier;
        this.energy = energy;
        this.buff = buff;
        this.source = source;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    public Map<Item, Integer> getIngredients() {
        return ingredientsSupplier.get();
    }

    public int Energy() {
        return energy;
    }

    public String Buff() {
        return buff;
    }

    public String Source() {
        return source;
    }

    @Override
    public int getPrice() {
        return price;
    }

    public static CookingRecipeType fromString(String name) {
        for (CookingRecipeType recipeType : CookingRecipeType.values()) {
            if (recipeType.getName().contains(name)) {
                return recipeType;
            }
        }
        return null;
    }
}

