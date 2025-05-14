package org.example.models.Enums;

import org.example.models.Craft;
import org.example.models.Database;
import org.example.models.Game;
import org.example.models.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum CraftType implements Material, Item {
    CherryBomb("Cherry Bomb", Map.of(Game.getDatabase().getItem("Copper Ore"), 4, MineralType.Coal, 1), "Mining Level 1", 50),
    Bomb("Bomb", Map.of(Game.getDatabase().getItem("Iron Ore"), 4, MineralType.Coal, 1), "Mining Level 2", 50),
    MegaBomb("Mega Bomb", Map.of(Game.getDatabase().getItem("Gold Ore"), 4, MineralType.Coal, 1), "Mining Level 3", 50),
    Sprinkler("Sprinkler", Map.of(MineralType.Copper, 1, MineralType.Iron, 1), "Farming Level 1", 0),
    QualitySprinkler("Quality Sprinkler", Map.of(MineralType.Iron, 1, MineralType.Gold, 1), "Farming Level 2", 0),
    IridiumSprinkler("Iridium Sprinkler", Map.of(MineralType.Gold, 1, MineralType.Iridium, 1), "Farming Level 3", 0),
    CharcoalKlin("Charcoal Klin", Map.of(Game.getDatabase().getItem("Wood"), 20, MineralType.Copper, 2), "Foraging Level 1", 0),
    Furnace("Furnace", Map.of(Game.getDatabase().getItem("Copper Ore"), 20, Game.getDatabase().getItem("Stone"), 25), "", 0),
    ScareCrow("Scarecrow", Map.of(Game.getDatabase().getItem("Wood"), 50, MineralType.Coal, 1, MineralType.Fiber, 20), "", 0),
    DeluxeScareCrow("Deluxe Scarecrow", Map.of(MineralType.Wood, 50, MineralType.Coal, 1, Game.getDatabase().getItem("Fiber"), 20, Game.getDatabase().getItem("Iridium Ore"), 1), "Farming Level 2", 0),
    BeeHouse("Bee House", Map.of(Game.getDatabase().getItem("Wood"), 40, MineralType.Coal, 8, MineralType.Iron, 1), "Farming Level 1", 0),
    CheesePress("Cheese Press", Map.of(Game.getDatabase().getItem("Wood"), 45, Game.getDatabase().getItem("Wood"), 45, MineralType.Copper, 1), "Farming Level 2", 0),
    Keg("Bee House", Map.of(Game.getDatabase().getItem("Wood"), 30, MineralType.Iron, 1, MineralType.Copper, 1), "Farming Level 3", 0),
    Loom("Loom", Map.of(Game.getDatabase().getItem("Wood"), 60, Game.getDatabase().getItem("Fiber"), 30), "Farming Level 3", 0),
    MayonnaiseMachine("Mayonnaise Machine", Map.of(Game.getDatabase().getItem("Wood"), 15, Game.getDatabase().getItem("Stone"), 15, MineralType.Copper, 1), "", 0),
    OilMaker("Oil Maker", Map.of(Game.getDatabase().getItem("Wood"), 100, MineralType.Gold, 1, MineralType.Iron, 1), "Farming Level 3", 0),
    PreservesJar("Preserves Jar", Map.of(Game.getDatabase().getItem("Wood"), 50, Game.getDatabase().getItem("Stone"), 40, MineralType.Coal, 8), "Farming Level 2", 0),
    Dehydrator("Dehydrator", Map.of(Game.getDatabase().getItem("Wood"), 30, Game.getDatabase().getItem("Stone"), 20, MineralType.Fiber, 30), "Pierre's General Store", 0),
    GrassStarter("Grass Starter", Map.of(Game.getDatabase().getItem("Wood"), 1,Game.getDatabase().getItem("Fiber"), 1), "Pierre's General Store", 0),
    FishSmoker("Fish Smoker", Map.of(Game.getDatabase().getItem("Wood"), 50, MineralType.Iron, 3, MineralType.Coal, 10), "Fish Shop", 0),
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
    public Database getDatabase() {
        return Game.getDatabase();
    }

}
