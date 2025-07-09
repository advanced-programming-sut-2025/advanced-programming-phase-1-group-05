package org.example.models.Enums;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.models.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public enum ForagingCrop implements Material, Item {
    CommonMushroom("Common Mushroom", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 40, 38,"Stardew_Valley_Images-main/Foraging/Common_Mushroom.png"),
    Daffodil("Daffodil", List.of(Season.SPRING), 30, 0,"Stardew_Valley_Images-main/Foraging/Daffodil.png"),
    Leek("Leek", List.of(Season.SPRING), 60, 40,"Stardew_Valley_Images-main/Foraging/Leek.png"),
    Morel("Morel", List.of(Season.SPRING), 150, 20,"Stardew_Valley_Images-main/Foraging/Morel.png"),
    SalmonBerry("Salmonberry", List.of(Season.SPRING), 5, 25,"Stardew_Valley_Images-main/Foraging/Salmonberry.png"),
    SpringOnion("Spring Onion", List.of(Season.SPRING), 8, 13,"Stardew_Valley_Images-main/Foraging/Spring_Onion.png"),
    WildHorseRadish("Wild Horseradish", List.of(Season.SPRING), 50, 13,"Stardew_Valley_Images-main/Foraging/Wild_Horseradish.png"),
    FiddleHeadFern("Fiddlehead Fern", List.of(Season.SUMMER), 90, 25,"Stardew_Valley_Images-main/Foraging/Fiddlehead_Fern.png"),
    Grape("Grape", List.of(Season.SUMMER), 80, 38,"Stardew_Valley_Images-main/Foraging/Grape.png"),
    RedMushroom("Red Mushroom", List.of(Season.SUMMER), 75, -50,"Stardew_Valley_Images-main/Foraging/Red_Mushroom.png"),
    SpiceBerry("Spice Berry", List.of(Season.SUMMER), 80, 25,"Stardew_Valley_Images-main/Foraging/Spice_Berry.png"),
    SweetPea("Sweet Pea", List.of(Season.SUMMER), 50, 0,"Stardew_Valley_Images-main/Foraging/Sweet_Pea.png"),
    BlackBerry("Blackberry", List.of(Season.FALL), 25, 25,"Stardew_Valley_Images-main/Foraging/Blackberry.png"),
    Chanterelle("Chanterelle", List.of(Season.FALL), 160, 75,"Stardew_Valley_Images-main/Foraging/Chanterelle.png"),
    Hazelnut("Hazelnut", List.of(Season.FALL), 40, 38,"Stardew_Valley_Images-main/Foraging/Hazelnut.png"),
    PurpleMushroom("Purple Mushroom", List.of(Season.FALL), 90, 30,"Stardew_Valley_Images-main/Foraging/Purple_Mushroom.png"),
    WildPlum("Wild Plum", List.of(Season.FALL), 80, 25,"Stardew_Valley_Images-main/Foraging/Wild_Plum.png"),
    Crocus("Crocus", List.of(Season.WINTER), 60, 0,"Stardew_Valley_Images-main/Foraging/Crocus.png"),
    CrystalFruit("Crystal Fruit", List.of(Season.WINTER), 150, 63,"Stardew_Valley_Images-main/Foraging/Crystal_Fruit.png"),
    Holly("Holly", List.of(Season.WINTER), 80, -37,"Stardew_Valley_Images-main/Foraging/Holly.png"),
    SnowYam("Snow Yam", List.of(Season.WINTER), 100, 30,"Stardew_Valley_Images-main/Foraging/Snow_Yam.png"),
    WinterRoot("Winter Root", List.of(Season.WINTER), 70, 25,"Stardew_Valley_Images-main/Foraging/Winter_Root.png"),
    Apricot("Apricot", List.of(Season.SPRING), 59, 38,"Stardew_Valley_Images-main/Trees/Apricot.png"),
    Cherry("Cherry", List.of(Season.SPRING), 80, 38,"Stardew_Valley_Images-main/Trees/Cherry.png"),
    Banana("Banana", List.of(Season.SUMMER), 150, 75,"Stardew_Valley_Images-main/Trees/Banana.png"),
    Mango("Mango", List.of(Season.SUMMER), 130, 100,"Stardew_Valley_Images-main/Trees/Mango.png"),
    Orange("Orange", List.of(Season.SUMMER), 100, 38,"Stardew_Valley_Images-main/Trees/Orange.png"),
    Peach("Peach", List.of(Season.SUMMER), 140, 38,"Stardew_Valley_Images-main/Trees/Peach.png"),
    Apple("Apple", List.of(Season.FALL), 100, 38,"Stardew_Valley_Images-main/Trees/Apple.png"),
    Pomegranate("Pomegranate", List.of(Season.FALL), 140, 38,"Stardew_Valley_Images-main/Trees/Pomegranate.png"),
    OakResin("Oak Resin", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 150, 0,"Stardew_Valley_Images-main/Trees/Oak_Resin.png"),
    MapleSyrup("Maple Syrup", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 200, 0,"Stardew_Valley_Images-main/Trees/Maple_Syrup.png"),
    Sap("Sap", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 2, -2,"Stardew_Valley_Images-main/Crafting/Sap.png"),
    MysticSyrup("Mystic Syrup", List.of(Season.SPRING, Season.SUMMER, Season.FALL, Season.WINTER), 1000, 500,"Stardew_Valley_Images-main/Trees/Mystic_Syrup.png");

    private final String name;
    private final List<Season> seasons;
    private final int price;
    private final int energy;
    private final String texturePath;
    ForagingCrop(String name, List<Season> seasons, int price, int energy, String texturePath) {
        this.name = name;
        this.seasons = seasons;
        this.price = price;
        this.energy = energy;
        this.texturePath = texturePath;
    }

    public String getName() {
        return name;
    }
    public boolean isAvailable(Season season) {
        return seasons.contains(season);
    }
    public List<Season> getSeasons() {
        return seasons;
    }
    public int getPrice() {
        return price;
    }
    public int getEnergy() {
        return energy;
    }

    public static ForagingCrop fromString(String name){
        for (ForagingCrop cropType : ForagingCrop.values()) {
            if (cropType.getName().equals(name)) {
                return cropType;
            }
        }
        return null;
    }
    public static ForagingCrop getRandomForagingCrop (Season currentSeason) {
        Random random = new Random();
        List<ForagingCrop> foragingTypes = new ArrayList<>();
        for (ForagingCrop f : ForagingCrop.values()) {
            if (f.getSeasons().contains(currentSeason)) foragingTypes.add(f);
        }
        while (true){
            int rand = random.nextInt(foragingTypes.size());
            ForagingCrop f = foragingTypes.get(rand);
            return f;
        }
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Name : ").append(name).append("\n")
                .append("Price : ").append(price).append("\n")
                .append("Energy : ").append(energy).append("\n")
                .append("Seasons : ");
        for (Season season : seasons) {
            builder.append(season).append(", ");
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
    public TextureRegion getTexture() {
        return new TextureRegion(new Texture(this.texturePath));
    }

}
