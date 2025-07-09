package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Item;

import java.util.Map;
import java.util.Random;

public enum MineralType implements Material, Item {
    Quartz("Quartz","A clear crystal commonly found in caves and mines.", 25,"Stardew_Valley_Images-main/Mineral/Quartz.png"),
    EarthCrystal("Earth Crystal","A resinous substance found near the surface.", 50,"Stardew_Valley_Images-main/Mineral/Earth_Crystal.png"),
    FrozenTear("Frozen Tear", "A crystal fabled to be the frozen tears of a yeti.", 75,"Stardew_Valley_Images-main/Mineral/Frozen_Tear.png"),
    FireQuartz("Fire Quartz","A glowing red crystal commonly found near hot lava.", 100,"Stardew_Valley_Images-main/Mineral/Fire_Quartz.png"),
    Emerald("Emerald","A precious stone with a brilliant green color.", 250,"Stardew_Valley_Images-main/Gem/Emerald.png"),
    Aquamarine("Aquamarine","A shimmery blue-green gem.", 180,"Stardew_Valley_Images-main/Gem/Aquamarine.png"),
    Ruby("Ruby","A precious stone that is sought after for its rich color and beautiful luster.", 250,"Stardew_Valley_Images-main/Gem/Ruby.png"),
    Amethyst("Amethyst","A purple variant of quartz.", 100,"Stardew_Valley_Images-main/Gem/Amethyst.png"),
    Topaz("Topaz", "Fairly common but still prized for its beauty.", 80,"Stardew_Valley_Images-main/Gem/Topaz.png"),
    Jade("Jade", "A pale green ornamental stone.", 200,"Stardew_Valley_Images-main/Gem/Jade.png"),
    Diamond("Diamond", "A rare and valuable gem.", 750,"Stardew_Valley_Images-main/Gem/Diamond.png"),
    PrismaticShard("Prismatic Shard", "A very rare and powerful substance with unknown origins.", 2000,"Stardew_Valley_Images-main/Gem/Prismatic_Shard.png"),
    Copper("Copper", "A common ore that can be smelted into bars.", 5,"Stardew_Valley_Images-main/Resource/Copper_Ore.png"),
    Iron("Iron", "A fairly common ore that can be smelted into bars.", 10,"Stardew_Valley_Images-main/Resource/Iron_Ore.png"),
    Gold("Gold", "A precious ore that can be smelted into bars.", 25,"Stardew_Valley_Images-main/Resource/Gold_Ore.png"),
    Iridium("Iridium", "An exotic ore with many curious properties. Can be smelted into bars.", 100,"Stardew_Valley_Images-main/Resource/Iridium_Ore.png"),
    Coal("Coal", "A combustible rock that is useful for crafting and smelting.", 15,"Stardew_Valley_Images-main/Resource/Coal.png"),
    Wood("Wood", "", 0,""),
    Stone("Stone", "", 0,""),
    Fiber("Fiber","",0,"");

    private final String name;
    private final String description;
    private final int price;
    private final String texturePath;
    MineralType(String name, String description, int price, String texturePath) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.texturePath = texturePath;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }

    public static MineralType getRandomMineralType() {
        Random random = new Random();
        MineralType[] types = MineralType.values();
        return types[random.nextInt(types.length)];
    }

    public static boolean isMineral(Material m) {
        for(MineralType t : MineralType.values()) {
            if(m.equals(t)) return true;
        }
        return false;
    }

    public static MineralType fromString(String name){
        for (MineralType type : MineralType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Name : " + getName() + "\nDescription : " + getDescription() + "\nPrice : " + getPrice();
    }
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(this.texturePath));
    }


}
