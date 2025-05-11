package org.example.models.Enums;

import java.util.List;
import java.util.Map;

public enum CookingRecipeType implements Material{
    FriedEgg("Fried Egg", Map.of(ForagingTreeSourceType.Acorns, 1),50, "", "Starter", 35),
    BakedFish("Fried Egg", Map.of(ForagingTreeSourceType.Acorns, 1),75, "", "Starter", 100),
    Salad("Fried Egg", Map.of(ForagingCrop.Leek, 1, ForagingCrop.Daffodil, 1),113, "", "Starter", 110),
    Omelet("Omelet", Map.of(ForagingTreeSourceType.Acorns, 1),100, "", "Stardrop Saloon", 125),
    PumpkinPie("Pumpkin Pie", Map.of(ForagingTreeSourceType.Acorns, 1),225, "", "Stardrop Saloon", 385),
    Spaghetti("Spaghetti", Map.of(ForagingTreeSourceType.Acorns, 1),75, "", "Stardrop Saloon", 120),
    Pizza("Pizza", Map.of(ForagingTreeSourceType.Acorns, 1),150, "", "Stardrop Saloon", 300),
    Tortilla("Tortilla", Map.of(ForagingTreeSourceType.Acorns, 1),50, "", "Stardrop Saloon", 50),
    MakiRoll("Maki Roll", Map.of(ForagingTreeSourceType.Acorns, 1),100, "", "Stardrop Saloon", 220),
    TripleShotEspresso("Triple Shot Espresso", Map.of(ForagingTreeSourceType.Acorns, 1),200, "Max Energy + 100 (5 hours)", "Stardrop Saloon", 450),
    Cookie("Cookie", Map.of(ForagingTreeSourceType.Acorns, 1),90, "", "Stardrop Saloon", 140),
    HashBrowns("Hash Browns", Map.of(ForagingTreeSourceType.Acorns, 1),90, "Farming (5 hours)", "Stardrop Saloon", 120),
    Pancakes("Pancakes", Map.of(ForagingTreeSourceType.Acorns, 1),90, "Foraging (11 hours)", "Stardrop Saloon", 80),
    FruitSalad("Fruit Salad", Map.of(ForagingTreeSourceType.Acorns, 1),263, "", "Stardrop Saloon", 450),
    RedPlate("Red Plate", Map.of(ForagingTreeSourceType.Acorns, 1),240, "Max Energy + 50 (3 hours)", "Stardrop Saloon", 400),
    Bread("Bread", Map.of(ForagingTreeSourceType.Acorns, 1),50, "", "Stardrop Saloon", 60),
    SalmonDinner("Salmon Dinner", Map.of(ForagingTreeSourceType.Acorns, 1),125, "", "Leah Reward", 300),
    VegetableMedley("Vegetable Medley", Map.of(ForagingTreeSourceType.Acorns, 1),165, "", "Foraging Level 2", 120),
    FarmersLunch("Farmer's Lunch", Map.of(ForagingTreeSourceType.Acorns, 1),200, "Farming (5 hours)", "SFarming level 1", 150),
    SurvivalBurger("Survival Burger", Map.of(ForagingTreeSourceType.Acorns, 1),125, "Foraging (5 hours)", "Foraging level 3", 180),
    DishOTheSea("Dish O' the Sea", Map.of(ForagingTreeSourceType.Acorns, 1),150, "Fishing (5 hours)", "Fishing level 2", 220),
    SeaformPudding("Seaform Pudding", Map.of(ForagingTreeSourceType.Acorns, 1),175, "Fishing (10 hours)", "Fishing level 3", 300),
    MinersTreat("Miner's Treat", Map.of(ForagingTreeSourceType.Acorns, 1),125, "Mining (5 hours)", "Mining level 1", 100);

    private final String name;
    private final Map<Material, Integer> ingredients;
    private final int enegry;
    private final String buff;
    private final String source;
    private final int price;
    CookingRecipeType(String name, Map<Material, Integer> ingredients, int enegy, String buff, String source, int price) {
        this.name = name;
        this.ingredients = ingredients;
        this.enegry = enegy;
        this.buff = buff;
        this.source = source;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }
    public Map<Material, Integer> getIngredients() {
        return ingredients;
    }
    public int Energy() {
        return enegry;
    }
    public String Buff() {
        return buff;
    }
    public String Source() {
        return source;
    }
    public static CookingRecipeType fromString(String name){
        for (CookingRecipeType recipeType : CookingRecipeType.values()) {
            if (recipeType.name().replaceAll("\\s", "").equalsIgnoreCase(name)) {
                return recipeType;
            }
        }
        return null;
    }
}
