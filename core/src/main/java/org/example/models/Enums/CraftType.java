package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.MyGame;
import org.example.models.Item;

import java.util.*;
import java.util.function.Supplier;

public enum CraftType implements Material, Item {
    CherryBomb("Cherry Bomb", () -> Map.of(MyGame.getDatabase().getItem("Copper Ore"), 4, MineralType.Coal, 1), "Mining Level 1", 50,"Stardew_Valley_Images-main/Weapon/Cherry_Bomb.png"),
    Bomb("Bomb", () -> Map.of(MyGame.getDatabase().getItem("Iron Ore"), 4, MineralType.Coal, 1), "Mining Level 2", 50,"Stardew_Valley_Images-main/Weapon/Bomb.png"),
    MegaBomb("Mega Bomb", () -> Map.of(MyGame.getDatabase().getItem("Gold Ore"), 4, MineralType.Coal, 1), "Mining Level 3", 50,"Stardew_Valley_Images-main/Weapon/Mega_Bomb.png"),
    Sprinkler("Sprinkler", () -> Map.of(MineralType.Copper, 1, MineralType.Iron, 1), "Farming Level 1", 0,"Stardew_Valley_Images-main/Crafting/Sprinkler.png"),
    QualitySprinkler("Quality Sprinkler", () -> Map.of(MineralType.Iron, 1, MineralType.Gold, 1), "Farming Level 2", 0,"Stardew_Valley_Images-main/Crafting/Quality_Sprinkler.png"),
    IridiumSprinkler("Iridium Sprinkler", () -> Map.of(MineralType.Gold, 1, MineralType.Iridium, 1), "Farming Level 3", 0,"Stardew_Valley_Images-main/Crafting/Iridium_Sprinkler.png"),
    CharcoalKlin("Charcoal Klin", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 20, MineralType.Copper, 2), "Foraging Level 1", 0,"Stardew_Valley_Images-main/Crafting/Charcoal_Kiln.png"),
    Furnace("Furnace", () -> Map.of(MyGame.getDatabase().getItem("Copper Ore"), 20, MyGame.getDatabase().getItem("Stone"), 25), "", 0,"Stardew_Valley_Images-main/Crafting/Furnace.png"),
    //ScareCrow("Scarecrow", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 50, MineralType.Coal, 1, MineralType.Fiber, 20), "", 0,"Stardew_Valley_Images-main/Crafting/Scarecrow.png"),
    //DeluxeScareCrow("Deluxe Scarecrow", () -> Map.of(MineralType.Wood, 50, MineralType.Coal, 1, MyGame.getDatabase().getItem("Fiber"), 20, MyGame.getDatabase().getItem("Iridium Ore"), 1), "Farming Level 2", 0,"Stardew_Valley_Images-main/Crafting/Deluxe_Scarecrow.png"),
    BeeHouse("Bee House", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 40, MineralType.Coal, 8, MineralType.Iron, 1), "Farming Level 1", 0,"Stardew_Valley_Images-main/Crafting/Bee_House.png"),
    CheesePress("Cheese Press", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 45, MineralType.Copper, 1), "Farming Level 2", 0,"Stardew_Valley_Images-main/Crafting/Cheese_Press.png"),
    Keg("Keg", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 30, MineralType.Iron, 1, MineralType.Copper, 1), "Farming Level 3", 0,"Stardew_Valley_Images-main/Crafting/Keg.png"),
    Loom("Loom", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 60, MyGame.getDatabase().getItem("Fiber"), 30), "Farming Level 3", 0,"Stardew_Valley_Images-main/Crafting/Loom.png"),
    MayonnaiseMachine("Mayonnaise Machine", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 15, MyGame.getDatabase().getItem("Stone"), 15, MineralType.Copper, 1), "", 0,"Stardew_Valley_Images-main/Crafting/Mayonnaise_Machine.png"),
    OilMaker("Oil Maker", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 100, MineralType.Gold, 1, MineralType.Iron, 1), "Farming Level 3", 0,"Stardew_Valley_Images-main/Crafting/Oil_Maker.png"),
    PreservesJar("Preserves Jar", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 50, MyGame.getDatabase().getItem("Stone"), 40, MineralType.Coal, 8), "Farming Level 2", 0,"Stardew_Valley_Images-main/Crafting/Preserves_Jar.png"),
    //Dehydrator("Dehydrator", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 30, MyGame.getDatabase().getItem("Stone"), 20, MineralType.Fiber, 30), "Pierre's General Store", 0,"Stardew_Valley_Images-main/Crafting/Dehydrator.png"),
    GrassStarter("Grass Starter", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 1, MyGame.getDatabase().getItem("Fiber"), 1), "Pierre's General Store", 0,"Stardew_Valley_Images-main/Crafting/Grass_Starter.png"),
    FishSmoker("Fish Smoker", () -> Map.of(MyGame.getDatabase().getItem("Wood"), 50, MineralType.Iron, 3, MineralType.Coal, 10), "Fish Shop", 0,"Stardew_Valley_Images-main/Crafting/Fish_Smoker.png"),
    MysticTreeSeed("Mystic Tree Seed", () -> Map.of(
            ForagingTreeSourceType.Acorns, 5,
            ForagingTreeSourceType.MapleSeeds, 5,
            ForagingTreeSourceType.MahoganySeeds, 5,
            ForagingTreeSourceType.PineCones, 5), "Foraging Level 4", 100,"Stardew_Valley_Images-main/Crafting/Mystic_Tree_Seed.png");

    private final String name;
    private final Supplier<Map<Item, Integer>> ingredientSupplier;
    private final String source;
    private final int price;
    private final String texturePath;

    CraftType(String name, Supplier<Map<Item, Integer>> ingredientSupplier, String source, int price, String texturePath) {
        this.name = name;
        this.ingredientSupplier = ingredientSupplier;
        this.source = source;
        this.price = price;
        this.texturePath = texturePath;
    }

    @Override
    public String getName() {
        return name;
    }

    public Map<Item, Integer> getIngredients() {
        return ingredientSupplier.get();
    }

    public String getSource() {
        return source;
    }

    public int getPrice() {
        return price;
    }

    public static CraftType fromString(String name) {
        for (CraftType craftType : CraftType.values()) {
            if (craftType.getName().equals(name)) {
                return craftType;
            }
        }
        return null;
    }
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(this.texturePath));
    }
}

