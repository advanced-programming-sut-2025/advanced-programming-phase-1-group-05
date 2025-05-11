package org.example.models.Enums;

import org.example.models.Craft;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CraftType implements Material {
    CherryBomb("Cherry Bomb", Map.of(MineralType.Copper, 4, MineralType.Coal, 1), "Mining Level 1", 50),
    Bomb("Bomb", Map.of(MineralType.Iron, 4, MineralType.Coal, 1), "Mining Level 2", 50),
    MegaBomb("Mega Bomb", Map.of(MineralType.Gold, 4, MineralType.Coal, 1), "Mining Level 3", 50),
    Sprinkler("Sprinkler", Map.of(MineralType.Copper, 1, MineralType.Iron, 1), "Farming Level 1", 0),
    QualitySprinkler("Quality Sprinkler", Map.of(MineralType.Iron, 1, MineralType.Gold, 1), "Farming Level 2", 0),
    IridiumSprinkler("Iridium Sprinkler", Map.of(MineralType.Gold, 1, MineralType.Iridium, 1), "Farming Level 3", 0),
    CharcoalKlin("Charcoal Klin", Map.of(MineralType.Wood, 20, MineralType.Copper, 2), "Foraging Level 1", 0),
    Furnace("Furnace", Map.of(MineralType.Copper, 20, MineralType.Stone, 25), "", 0),
    ScareCrow("Scarecrow", Map.of(MineralType.Wood, 50, MineralType.Coal, 1, MineralType.Fiber, 20), "", 0),
    DeluxeScareCrow("Deluxe Scarecrow", Map.of(MineralType.Wood, 50, MineralType.Coal, 1, MineralType.Fiber, 20, MineralType.Iridium, 1), "Farming Level 2", 0),
    BeeHouse("Bee House", Map.of(MineralType.Wood, 40, MineralType.Coal, 8, MineralType.Iron, 1), "Farming Level 1", 0),
    CheesePress("Cheese Press", Map.of(MineralType.Wood, 45, MineralType.Stone, 45, MineralType.Copper, 1), "Farming Level 2", 0),
    Keg("Bee House", Map.of(MineralType.Wood, 30, MineralType.Iron, 1, MineralType.Copper, 1), "Farming Level 3", 0),
    Loom("Loom", Map.of(MineralType.Wood, 60, MineralType.Fiber, 30), "Farming Level 3", 0),
    MayonnaiseMachine("Mayonnaise Machine", Map.of(MineralType.Wood, 15, MineralType.Stone, 15, MineralType.Copper, 1), "", 0),
    OilMaker("Oil Maker", Map.of(MineralType.Wood, 100, MineralType.Gold, 1, MineralType.Iron, 1), "Farming Level 3", 0),
    PreservesJar("Preserves Jar", Map.of(MineralType.Wood, 50, MineralType.Stone, 40, MineralType.Coal, 8), "Farming Level 2", 0),
    Dehydrator("Preserves Jar", Map.of(MineralType.Wood, 30, MineralType.Stone, 20, MineralType.Fiber, 30), "Pierre's General Store", 0),
    GrassStarter("Grass Starter", Map.of(MineralType.Wood, 1, MineralType.Fiber, 1), "Pierre's General Store", 0),
    FishSmoker("Fish Smoker", Map.of(MineralType.Wood, 50, MineralType.Iron, 3, MineralType.Coal, 10), "Fish Shop", 0),
    MysticTreeSeed("Mystic Tree Seed", Map.of(ForagingTreeSourceType.Acorns, 5, ForagingTreeSourceType.MapleSeeds, 5,
            ForagingTreeSourceType.MahoganySeeds, 5, ForagingTreeSourceType.PineCones, 5), "Foraging Level 4", 100);

    private final String name;
    private final Map<Material, Integer> ingredients;
    private final String source;
    private final int price;

    CraftType(String name, Map<Material, Integer> ingredients, String source, int price) {
        this.name = name;
        this.ingredients = ingredients;
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
    public String getSource() {
        return source;
    }
    public int getPrice() {
        return price;
    }
    public static CraftType fromString(String name){
        for (CraftType craftType : CraftType.values()) {
            if (craftType.name().replaceAll("\\s", "").equalsIgnoreCase(name)) {
                return craftType;
            }
        }
        return null;
    }

}
