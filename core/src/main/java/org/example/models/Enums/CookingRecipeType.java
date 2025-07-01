package org.example.models.Enums;

import org.example.models.MyGame;
import org.example.models.Item;

import java.util.Map;
import java.util.function.Supplier;

public enum CookingRecipeType implements Material, Item {
    FriedEgg("Fried Egg", () -> Map.of(MyGame.getDatabase().getItem("Egg"), 1), 50, false, "Starter", 35),
    BakedFish("Baked Fish", () -> Map.of(Fish.Sardine, 1, Fish.Salmon, 1, CropType.Wheat, 1), 75, false, "Starter", 100),
    Salad("Salad", () -> Map.of(ForagingCrop.Leek, 1, MyGame.getDatabase().getItem("Dandelion"), 1), 113, false, "Starter", 110),
    Omelet("Omelet", () -> Map.of(MyGame.getDatabase().getItem("Egg"), 1, MyGame.getDatabase().getItem("Milk"), 1), 100, false, "Stardrop Saloon", 125),
    PumpkinPie("Pumpkin Pie", () -> Map.of(
            CropType.Pumpkin, 1,
            MyGame.getDatabase().getItem("Wheat Flour"), 1,
            MyGame.getDatabase().getItem("Milk"), 1,
            MyGame.getDatabase().getItem("Sugar"), 1), 225, false, "Stardrop Saloon", 385),

    Spaghetti("Spaghetti", () -> Map.of(MyGame.getDatabase().getItem("Wheat Flour"), 1, CropType.Tomato, 1), 75, false, "Stardrop Saloon", 120),
    Pizza("Pizza", () -> Map.of(MyGame.getDatabase().getItem("Wheat Flour"), 1, CropType.Tomato, 1, MyGame.getDatabase().getItem("Cheese"), 1), 150, false, "Stardrop Saloon", 300),
    Tortilla("Tortilla", () -> Map.of(CropType.Corn, 1), 50, false, "Stardrop Saloon", 50),
    MakiRoll("Maki Roll", () -> Map.of(Fish.Sardine, 1, MyGame.getDatabase().getItem("Rice"), 1, MyGame.getDatabase().getItem("Fiber"), 1), 100, false, "Stardrop Saloon", 220),
    TripleShotEspresso("Triple Shot Espresso", () -> Map.of(MyGame.getDatabase().getItem("Coffee"), 3), 200, true, "Stardrop Saloon", 450),
    Cookie("Cookie", () -> Map.of(MyGame.getDatabase().getItem("Wheat Flour"), 1, MyGame.getDatabase().getItem("Sugar"), 1, MyGame.getDatabase().getItem("Egg"), 1), 90, false, "Stardrop Saloon", 140),
    HashBrowns("Hash Browns", () -> Map.of(CropType.Potato, 1, MyGame.getDatabase().getItem("Oil"), 1), 90, true, "Stardrop Saloon", 120),
    Pancakes("Pancakes", () -> Map.of(MyGame.getDatabase().getItem("Wheat Flour"), 1, MyGame.getDatabase().getItem("Egg"), 1), 90, true, "Stardrop Saloon", 80),
    FruitSalad("Fruit Salad", () -> Map.of(CropType.Blueberry, 1, CropType.Melon, 1, MyGame.getDatabase().getItem("Apricot"), 1), 263, false, "Stardrop Saloon", 450),
    RedPlate("Red Plate", () -> Map.of(CropType.RedCabbage, 1, CropType.Radish, 1), 240, true, "Stardrop Saloon", 400),
    Bread("Bread", () -> Map.of(MyGame.getDatabase().getItem("Wheat Flour"), 1), 50, false, "Stardrop Saloon", 60),
    SalmonDinner("Salmon Dinner", () -> Map.of(Fish.Salmon, 1, CropType.Amaranth, 1, CropType.Kale, 1), 125, false, "Leah Reward", 300),
    VegetableMedley("Vegetable Medley", () -> Map.of(CropType.Tomato, 1, CropType.Beet, 1), 165, false, "Foraging Level 2", 120),
    FarmersLunch("Farmer's Lunch", () -> Map.of(CropType.HotPepper, 1, CropType.Parsnip, 1), 200, true, "Farming level 1", 150),
    SurvivalBurger("Survival Burger", () -> Map.of(CropType.HotPepper, 1, CropType.Carrot, 1, CropType.Eggplant, 1), 125, true, "Foraging level 3", 180),
    DishOTheSea("Dish O' the Sea", () -> Map.of(Fish.Sardine, 2, CropType.HotPepper, 1), 150, true, "Fishing level 2", 220),
    SeaformPudding("Seaform Pudding", () -> Map.of(Fish.Flounder, 1, Fish.MidnightCarp, 1), 175, true, "Fishing level 3", 300),
    MinersTreat("Miner's Treat", () -> Map.of(CropType.Carrot, 1, MyGame.getDatabase().getItem("Egg"), 1, MyGame.getDatabase().getItem("Milk"), 1), 125, true, "Mining level 1", 100);

    private final String name;
    private final Supplier<Map<Item, Integer>> ingredientsSupplier;
    private final int energy;
    private final boolean buffMaxEnergy;
    private final String source;
    private final int price;

    CookingRecipeType(String name, Supplier<Map<Item, Integer>> ingredientsSupplier, int energy, boolean buff, String source, int price) {
        this.name = name;
        this.ingredientsSupplier = ingredientsSupplier;
        this.energy = energy;
        this.buffMaxEnergy = buff;
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

    public boolean Buff() {
        return buffMaxEnergy;
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

