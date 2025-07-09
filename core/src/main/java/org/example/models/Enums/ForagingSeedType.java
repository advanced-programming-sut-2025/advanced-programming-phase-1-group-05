package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sun.source.tree.Tree;
import org.example.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ForagingSeedType implements Material, Item {
    JazzSeeds("Jazz Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Jazz_Seeds.png"),
    CarrotSeeds("Carrot Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Carrot_Seeds.png"),
    CauliflowerSeeds("Cauliflower Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Cauliflower_Seeds.png"),
    CoffeeBean("Coffee Bean", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Coffee_Bean.png"),
    GarlicSeeds("Garlic Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Garlic_Seeds.png"),
    BeanStarter("Bean Starter", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Bean_Starter.png"),
    KaleSeeds("Kale Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Kale_Seeds.png"),
    ParsnipSeeds("Parsnip Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Parsnip_Seeds.png"),
    PotatoSeeds("Potato Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Potato_Seeds.png"),
    RhubarbSeeds("Rhubarb Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Rhubarb_Seeds.png"),
    StrawberrySeeds("Strawberry Seeds", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Strawberry_Seeds.png"),
    TulipBulb("Tulip Bulb", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Tulip_Bulb.png"),
    RiceShoot("Rice Shoot", List.of(Season.SPRING),"Stardew_Valley_Images-main/Crops/Rice_Shoot.png"),
    BlueBerrySeeds("Blueberry Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Blueberry_Seeds.png"),
    CornSeeds("Corn Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Corn_Seeds.png"),
    HopsStarter("Hops Starter", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Hops_Starter.png"),
    PepperSeeds("Pepper Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Pepper_Seeds.png"),
    MelonSeeds("Melon Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Melon_Seeds.png"),
    PoppySeeds("Poppy Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Poppy_Seeds.png"),
    RadishSeeds("Radish Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Radish_Seeds.png"),
    RedCabbageSeeds("Red Cabbage Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Red_Cabbage_Seeds.png"),
    StarFruitSeeds("Starfruit Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Starfruit_Seeds.png"),
    SpangleSeeds("Spangle Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Spangle_Seeds.png"),
    SummerSquashSeeds("Summer Squash Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Summer_Squash_Seeds.png"),
    SunflowerSeeds("Sunflower Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Sunflower_Seeds.png"),
    TomatoSeeds("Tomato Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Tomato_Seeds.png"),
    WheatSeeds("Wheat Seeds", List.of(Season.SUMMER),"Stardew_Valley_Images-main/Crops/Wheat_Seeds.png"),
    AmaranthSeeds("Amaranth Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Amaranth_Seeds.png"),
    ArtichokeSeeds("Artichoke Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Artichoke_Seeds.png"),
    BeetSeeds("Beet Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Beet_Seeds.png"),
    BokChoySeeds("Bok Choy Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Bok_Choy_Seeds.png"),
    BroccoliSeeds("Broccoli Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Broccoli_Seeds.png"),
    CranberrySeeds("Cranberry Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Cranberry_Seeds.png"),
    EggplantSeeds("Eggplant Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Eggplant_Seeds.png"),
    FairySeeds("Fairy Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Fairy_Seeds.png"),
    GrapeStarter("Grape Starter", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Grape_Starter.png"),
    PumpkinSeeds("Pumpkin Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Pumpkin_Seeds.png"),
    YamSeeds("Yam Seeds", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Yam_Seeds.png"),
    RareSeeds("Rare Seed", List.of(Season.FALL),"Stardew_Valley_Images-main/Crops/Rare_Seed.png"),
    PowdermelonSeeds("Powdermelon Seeds", List.of(Season.WINTER),"Stardew_Valley_Images-main/Crops/Powdermelon_Seeds.png"),
    AncientSeeds("Ancient Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),"Stardew_Valley_Images-main/Crops/Ancient_Seeds.png"),
    MixedSeeds("Mixed Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER),"Stardew_Valley_Images-main/Crops/Mixed_Seeds.png");

    private final String name;
    private final List<Season> seasons;
    private final String texturePath;
    ForagingSeedType(String name, List<Season> seasons, String texturePath) {
        this.name = name;
        this.seasons = seasons;
        this.texturePath = texturePath;
    }

    public String getName() {
        return name;
    }
    public List<Season> getSeasons() {
        return seasons;
    }

    public static ForagingSeedType fromString(String name){
        for (ForagingSeedType seedType : ForagingSeedType.values()) {
            if (seedType.getName().equals(name)) {
                return seedType;
            }
        }
        return null;
    }

    public static ForagingSeedType getRandomForagingSeedType (Season currentSeason) {
        Random random = new Random();
        List<ForagingSeedType> foragingSeedTypes = new ArrayList<>();
        for (ForagingSeedType f : ForagingSeedType.values()) {
            if (f.getSeasons().contains(currentSeason)) foragingSeedTypes.add(f);
        }
        while (true){
            int rand = random.nextInt(foragingSeedTypes.size());
            ForagingSeedType f = foragingSeedTypes.get(rand);
            return f;
        }
    }
    @Override
    public int getPrice() {
        return 0;
    }
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name : ").append(name).append("\n").append("Seasons : ");
        for (Season season : seasons) {
            stringBuilder.append(season).append(", ");
        }
        return stringBuilder.toString();
    }
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(this.texturePath));
    }

}
