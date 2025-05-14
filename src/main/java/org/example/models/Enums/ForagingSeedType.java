package org.example.models.Enums;

import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ForagingSeedType implements Material {
    JazzSeeds("Jazz Seeds", List.of(Season.SPRING)),
    CarrotSeeds("Carrot Seeds", List.of(Season.SPRING)),
    CauliflowerSeeds("Cauliflower Seeds", List.of(Season.SPRING)),
    CoffeeBean("Coffee Bean", List.of(Season.SPRING)),
    GarlicSeeds("Garlic Seeds", List.of(Season.SPRING)),
    BeanStarter("Bean Starter", List.of(Season.SPRING)),
    KaleSeeds("Kale Seeds", List.of(Season.SPRING)),
    ParsnipSeeds("Parsnip Seeds", List.of(Season.SPRING)),
    PotatoSeeds("Potato Seeds", List.of(Season.SPRING)),
    RhubarbSeeds("Rhubarb Seeds", List.of(Season.SPRING)),
    StrawberrySeeds("Strawberry Seeds", List.of(Season.SPRING)),
    TulipBulb("Tulip Bulb", List.of(Season.SPRING)),
    RiceShoot("Rice Shoot", List.of(Season.SPRING)),
    BlueBerrySeeds("Blueberry Seeds", List.of(Season.SUMMER)),
    CornSeeds("Corn Seeds", List.of(Season.SUMMER)),
    HopsStarter("Hops Starter", List.of(Season.SUMMER)),
    PepperSeeds("Pepper Seeds", List.of(Season.SUMMER)),
    MelonSeeds("Melon Seeds", List.of(Season.SUMMER)),
    PoppySeeds("Poppy Seeds", List.of(Season.SUMMER)),
    RadishSeeds("Radish Seeds", List.of(Season.SUMMER)),
    RedCabbageSeeds("Red Cabbage Seeds", List.of(Season.SUMMER)),
    StarFruitSeeds("Starfruit Seeds", List.of(Season.SUMMER)),
    SpangleSeeds("Spangle Seeds", List.of(Season.SUMMER)),
    SummerSquashSeeds("Summer Squash Seeds", List.of(Season.SUMMER)),
    SunflowerSeeds("Sunflower Seeds", List.of(Season.SUMMER)),
    TomatoSeeds("Tomato Seeds", List.of(Season.SUMMER)),
    WheatSeeds("Wheat Seeds", List.of(Season.SUMMER)),
    AmaranthSeeds("Amaranth Seeds", List.of(Season.FALL)),
    ArtichokeSeeds("Artichoke Seeds", List.of(Season.FALL)),
    BeetSeeds("Beet Seeds", List.of(Season.FALL)),
    BokChoySeeds("Bok Choy Seeds", List.of(Season.FALL)),
    BroccoliSeeds("Broccoli Seeds", List.of(Season.FALL)),
    CranberrySeeds("Cranberry Seeds", List.of(Season.FALL)),
    EggplantSeeds("Eggplant Seeds", List.of(Season.FALL)),
    FairySeeds("Fairy Seeds", List.of(Season.FALL)),
    GrapeStarter("Grape Starter", List.of(Season.FALL)),
    PumpkinSeeds("Pumpkin Seeds", List.of(Season.FALL)),
    YamSeeds("Yam Seeds", List.of(Season.FALL)),
    RareSeeds("Rare Seed", List.of(Season.FALL)),
    PowdermelonSeeds("Powdermelon Seeds", List.of(Season.WINTER)),
    AncientSeeds("Ancient Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER)),
    MixedSeeds("Mixed Seeds", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER));

    private final String name;
    private final List<Season> seasons;
    ForagingSeedType(String name, List<Season> seasons) {
        this.name = name;
        this.seasons = seasons;
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
}
