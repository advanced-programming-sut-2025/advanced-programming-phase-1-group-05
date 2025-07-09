package org.example.models.Enums;

import org.example.models.ArtisanProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public enum ArtisanType {
    BEE_HOUSE(
            List.of(
                    createRecipe(Map.of(), new ArtisanProduct("Honey", 75, 96, 350))
            )),
    CHEESE_PRESS(
            List.of(
                    createRecipe(Map.of("large milk", 1), new ArtisanProduct("Cheese", 100, 3, 345)),
                    createRecipe(Map.of("goat milk", 1), new ArtisanProduct("Goat Cheese", 100, 3, 400)),
                    createRecipe(Map.of("large goat milk", 1), new ArtisanProduct("Goat Cheese", 100, 3, 600)),
                    createRecipe(Map.of("milk", 1), new ArtisanProduct("Cheese", 100, 3, 230)))
    ),
    Keg(List.of(Map.of(Map.of("Wheat", 1), new ArtisanProduct("Beer", 50, 24, 200)),
            Map.of(Map.of("rice", 1), new ArtisanProduct("Vinegar", 13, 10, 100)),
            Map.of(Map.of("Coffee bean", 5), new ArtisanProduct("Coffee", 75, 2, 150)),
            Map.of(Map.of("Amaranth", 1), new ArtisanProduct("Juice", 2 * CropType.Amaranth.getEnergy(), 96, (int) (2.25 * CropType.Amaranth.getPrice()))),
            Map.of(Map.of("Artichoke", 1), new ArtisanProduct("Juice", 2 * CropType.Artichoke.getEnergy(), 96, (int) (2.25 * CropType.Artichoke.getPrice()))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Juice", 2 * CropType.Beet.getEnergy(), 96, (int) (2.25 * CropType.Beet.getPrice()))),
            Map.of(Map.of("Bok Choy", 1), new ArtisanProduct("Juice", 2 * CropType.BokChoy.getEnergy(), 96, (int) (2.25 * CropType.BokChoy.getPrice()))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Juice", 2 * CropType.Beet.getEnergy(), 96, (int) (2.25 * CropType.Beet.getPrice()))),
            Map.of(Map.of("Broccoli", 1), new ArtisanProduct("Juice", 2 * CropType.Broccoli.getEnergy(), 96, (int) (2.25 * CropType.Broccoli.getPrice()))),
            Map.of(Map.of("Carrot", 1), new ArtisanProduct("Juice", 2 * CropType.Carrot.getEnergy(), 96, (int) (2.25 * CropType.Carrot.getPrice()))),
            Map.of(Map.of("Cauliflower", 1), new ArtisanProduct("Juice", 2 * CropType.Cauliflower.getEnergy(), 96, (int) (2.25 * CropType.Cauliflower.getPrice()))),
            Map.of(Map.of("Corn", 1), new ArtisanProduct("Juice", 2 * CropType.Corn.getEnergy(), 96, (int) (2.25 * CropType.Corn.getPrice()))),
            Map.of(Map.of("Eggplant", 1), new ArtisanProduct("Juice", 2 * CropType.Eggplant.getEnergy(), 96, (int) (2.25 * CropType.Eggplant.getPrice()))),
            Map.of(Map.of("Garlic", 1), new ArtisanProduct("Juice", 2 * CropType.Garlic.getEnergy(), 96, (int) (2.25 * CropType.Garlic.getPrice()))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Juice", 2 * CropType.Beet.getEnergy(), 96, (int) (2.25 * CropType.Beet.getPrice()))),
            Map.of(Map.of("Green Bean", 1), new ArtisanProduct("Juice", 2 * CropType.GreenBean.getEnergy(), 96, (int) (2.25 * CropType.GreenBean.getPrice()))),
            Map.of(Map.of("Hops", 1), new ArtisanProduct("Juice", 2 * CropType.Hops.getEnergy(), 96, (int) (2.25 * CropType.Hops.getPrice()))),
            Map.of(Map.of("Kale", 1), new ArtisanProduct("Juice", 2 * CropType.Kale.getEnergy(), 96, (int) (2.25 * CropType.Kale.getPrice()))),
            Map.of(Map.of("Parsnip", 1), new ArtisanProduct("Juice", 2 * CropType.Parsnip.getEnergy(), 96, (int) (2.25 * CropType.Parsnip.getPrice()))),
            Map.of(Map.of("Potato", 1), new ArtisanProduct("Juice", 2 * CropType.Potato.getEnergy(), 96, (int) (2.25 * CropType.Potato.getPrice()))),
            Map.of(Map.of("Pumpkin", 1), new ArtisanProduct("Juice", 2 * CropType.Pumpkin.getEnergy(), 96, (int) (2.25 * CropType.Pumpkin.getPrice()))),
            Map.of(Map.of("Radish", 1), new ArtisanProduct("Juice", 2 * CropType.Radish.getEnergy(), 96, (int) (2.25 * CropType.Radish.getPrice()))),
            Map.of(Map.of("Red Cabbage", 1), new ArtisanProduct("Juice", 2 * CropType.RedCabbage.getEnergy(), 96, (int) (2.25 * CropType.RedCabbage.getPrice()))),
            Map.of(Map.of("Summer Squash", 1), new ArtisanProduct("Juice", 2 * CropType.SummerSquash.getEnergy(), 96, (int) (2.25 * CropType.SummerSquash.getPrice()))),
            Map.of(Map.of("Tomato", 1), new ArtisanProduct("Juice", 2 * CropType.Tomato.getEnergy(), 96, (int) (2.25 * CropType.Tomato.getPrice()))),
            Map.of(Map.of("Unmilled Rice", 1), new ArtisanProduct("Juice", 2 * CropType.UnmilledRice.getEnergy(), 96, (int) (2.25 * CropType.UnmilledRice.getPrice()))),
            Map.of(Map.of("Wheat", 1), new ArtisanProduct("Juice", 2 * CropType.Wheat.getEnergy(), 96, (int) (2.25 * CropType.Wheat.getPrice()))),
            Map.of(Map.of("Yam", 1), new ArtisanProduct("Juice", 2 * CropType.Yam.getEnergy(), 96, (int) (2.25 * CropType.Yam.getPrice()))),
            Map.of(Map.of("Honey", 1), new ArtisanProduct("Mead", 100, 10, 300)),
            Map.of(Map.of("Hops", 1), new ArtisanProduct("Pale Ale", 50, 72, 300)),
            Map.of(Map.of("Ancient fruit", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.AncientFruit.getEnergy()), 168, 3 * CropType.AncientFruit.getPrice())),
            Map.of(Map.of("Blueberry", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.Blueberry.getEnergy()), 168, 3 * CropType.Blueberry.getPrice())),
            Map.of(Map.of("Cranberry", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.Cranberries.getEnergy()), 168, 3 * CropType.Cranberries.getPrice())),
            Map.of(Map.of("Grape", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.Grape.getEnergy()), 168, 3 * CropType.Grape.getPrice())),
            Map.of(Map.of("Hot Pepper", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.HotPepper.getEnergy()), 168, 3 * CropType.HotPepper.getPrice())),
            Map.of(Map.of("Melon", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.Melon.getEnergy()), 168, 3 * CropType.Melon.getPrice())),
            Map.of(Map.of("Powder Melon", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.PowderMelon.getEnergy()), 168, 3 * CropType.PowderMelon.getPrice())),
            Map.of(Map.of("Rhubarb", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.Rhubarb.getEnergy()), 168, 3 * CropType.Rhubarb.getPrice())),
            Map.of(Map.of("StarFruit", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.StarFruit.getEnergy()), 168, 3 * CropType.StarFruit.getPrice())),
            Map.of(Map.of("Strawberry", 1), new ArtisanProduct("Wine", (int) (1.75 * CropType.Strawberry.getEnergy()), 168, 3 * CropType.Strawberry.getPrice())))),
    Dehydrator(List.of(Map.of(Map.of("Chanterelle", 1), new ArtisanProduct("Dried Mushrooms", 50, 10, (int) ((7.5 * ForagingCrop.Chanterelle.getPrice()) + 25))),
            Map.of(Map.of("Common Mushroom", 1), new ArtisanProduct("Dried Mushrooms", 50, 10, (int) ((7.5 * ForagingCrop.Chanterelle.getPrice()) + 25))),
            Map.of(Map.of("Morel", 1), new ArtisanProduct("Dried Mushrooms", 50, 10, (int) ((7.5 * ForagingCrop.Morel.getPrice()) + 25))),
            Map.of(Map.of("Purple Mushroom", 1), new ArtisanProduct("Dried Mushrooms", 50, 10, (int) ((7.5 * ForagingCrop.PurpleMushroom.getPrice()) + 25))),
            Map.of(Map.of("Red Mushroom", 1), new ArtisanProduct("Dried Mushrooms", 50, 10, (int) ((7.5 * ForagingCrop.RedMushroom.getPrice()) + 25)))
    )),
    CharcoalKlin(List.of(Map.of(Map.of("Wood", 10), new ArtisanProduct("Coal", -1, 1, 50)))),
    Loom(List.of(Map.of(Map.of("Wool", 1), new ArtisanProduct("Cloth", -1, 4, 470)))),
    MayonnaiseMachine(List.of(Map.of(Map.of("Egg", 1), new ArtisanProduct("Mayonnaise", 50, 3, 190)),
            Map.of(Map.of("Large Egg", 1), new ArtisanProduct("Mayonnaise", 50, 3, 237)),
            Map.of(Map.of("Duck Egg", 1), new ArtisanProduct("Duck Mayonnaise", 75, 3, 37)),
            Map.of(Map.of("Dinosaur Egg", 1), new ArtisanProduct("Dinosaur Mayonnaise", 125, 3, 800)))),
    OilMaker(List.of(Map.of(Map.of("Truffle", 1), new ArtisanProduct("Truffle Oil", 38, 6, 1065)),
            Map.of(Map.of("Corn", 1), new ArtisanProduct("Oil", 13, 6, 100)),
            Map.of(Map.of("Sunflower Seeds", 1), new ArtisanProduct("Oil", 13, 48, 100)),
            Map.of(Map.of("Sunflower", 1), new ArtisanProduct("Oil", 13, 1, 100)))),
    PreservesJar(List.of(Map.of(Map.of("Amaranth", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Amaranth.getEnergy()), 96, (int) (2 * CropType.Amaranth.getPrice() + 50))),
            Map.of(Map.of("Artichoke", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Artichoke.getEnergy()), 96, (int) (2 * CropType.Artichoke.getPrice() + 50))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Beet.getEnergy()), 96, (int) (2 * CropType.Beet.getPrice() + 50))),
            Map.of(Map.of("Bok Choy", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.BokChoy.getEnergy()), 96, (int) (2 * CropType.BokChoy.getPrice() + 50))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Beet.getEnergy()), 96, (int) (2 * CropType.Beet.getPrice() + 50))),
            Map.of(Map.of("Broccoli", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Broccoli.getEnergy()), 96, (int) (2 * CropType.Broccoli.getPrice() + 50))),
            Map.of(Map.of("Carrot", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Carrot.getEnergy()), 96, (int) (2 * CropType.Carrot.getPrice() + 50))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Beet.getEnergy()), 96, (int) (2 * CropType.Beet.getPrice() + 50))),
            Map.of(Map.of("Cauliflower", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Cauliflower.getEnergy()), 96, (int) (2 * CropType.Cauliflower.getPrice() + 50))),
            Map.of(Map.of("Corn", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Corn.getEnergy()), 96, (int) (2 * CropType.Corn.getPrice() + 50))),
            Map.of(Map.of("Eggplant", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Eggplant.getEnergy()), 96, (int) (2 * CropType.Eggplant.getPrice() + 50))),
            Map.of(Map.of("Garlic", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Garlic.getEnergy()), 96, (int) (2 * CropType.Garlic.getPrice() + 50))),
            Map.of(Map.of("Beet", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Beet.getEnergy()), 96, (int) (2 * CropType.Beet.getPrice() + 50))),
            Map.of(Map.of("Green Bean", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.GreenBean.getEnergy()), 96, (int) (2 * CropType.GreenBean.getPrice() + 50))),
            Map.of(Map.of("Hops", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Hops.getEnergy()), 96, (int) (2 * CropType.Hops.getPrice() + 50))),
            Map.of(Map.of("Kale", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Kale.getEnergy()), 96, (int) (2 * CropType.Kale.getPrice() + 50))),
            Map.of(Map.of("Parsnip", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Parsnip.getEnergy()), 96, (int) (2 * CropType.Parsnip.getPrice() + 50))),
            Map.of(Map.of("Potato", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Potato.getEnergy()), 96, (int) (2 * CropType.Potato.getPrice() + 50))),
            Map.of(Map.of("Pumpkin", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Pumpkin.getEnergy()), 96, (int) (2 * CropType.Pumpkin.getPrice() + 50))),
            Map.of(Map.of("Radish", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Radish.getEnergy()), 96, (int) (2 * CropType.Radish.getPrice() + 50))),
            Map.of(Map.of("Red Cabbage", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.RedCabbage.getEnergy()), 96, (int) (2 * CropType.RedCabbage.getPrice() + 50))),
            Map.of(Map.of("Summer Squash", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.SummerSquash.getEnergy()), 96, (int) (2 * CropType.SummerSquash.getPrice() + 50))),
            Map.of(Map.of("Tomato", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Tomato.getEnergy()), 96, (int) (2 * CropType.Tomato.getPrice() + 50))),
            Map.of(Map.of("Unmilled Rice", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.UnmilledRice.getEnergy()), 96, (int) (2 * CropType.UnmilledRice.getPrice() + 50))),
            Map.of(Map.of("Wheat", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Wheat.getEnergy()), 96, (int) (2 * CropType.Wheat.getPrice() + 50))),
            Map.of(Map.of("Yam", 1), new ArtisanProduct("Pickles", (int) (1.75 * CropType.Yam.getEnergy()), 96, (int) (2 * CropType.Yam.getPrice() + 50))))),
    FishSmoker(List.of(Map.of(Map.of("Coal", 1, "Salmon", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Salmon.getPrice())),
            Map.of(Map.of("Coal", 1, "Sardine", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Sardine.getPrice())),
            Map.of(Map.of("Coal", 1, "Shad", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Shad.getPrice())),
            Map.of(Map.of("Coal", 1, "Blue Discus", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.BlueDiscus.getPrice())),
            Map.of(Map.of("Coal", 1, "Midnight Carp", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.MidnightCarp.getPrice())),
            Map.of(Map.of("Coal", 1, "Squid", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Squid.getPrice())),
            Map.of(Map.of("Coal", 1, "Tuna", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Tuna.getPrice())),
            Map.of(Map.of("Coal", 1, "Perch", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Perch.getPrice())),
            Map.of(Map.of("Coal", 1, "Flounder", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Flounder.getPrice())),
            Map.of(Map.of("Coal", 1, "Lion Fish", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Lionfish.getPrice())),
            Map.of(Map.of("Coal", 1, "Herring", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Herring.getPrice())),
            Map.of(Map.of("Coal", 1, "Ghost Fish", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.GhostFish.getPrice())),
            Map.of(Map.of("Coal", 1, "Tilapia", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Tilapia.getPrice())),
            Map.of(Map.of("Coal", 1, "Dorado", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Dorado.getPrice())),
            Map.of(Map.of("Coal", 1, "Sun Fish", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Sunfish.getPrice())),
            Map.of(Map.of("Coal", 1, "Rainbow Trout", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.RainbowTrout.getPrice())),
            Map.of(Map.of("Coal", 1, "Legend", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Legend.getPrice())),
            Map.of(Map.of("Coal", 1, "Glacier Fish", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.GlacierFish.getPrice())),
            Map.of(Map.of("Coal", 1, "Angler", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.Angler.getPrice())),
            Map.of(Map.of("Coal", 1, "Crimson Fish", 1), new ArtisanProduct("Smoked Fish", 150, 1, 2 * Fish.CrimsonFish.getPrice())))),
    Furnace(List.of(Map.of(Map.of("Coal", 1, "Copper Ore", 5), new ArtisanProduct("Copper Bar", -1, 4, 10 * MineralType.Copper.getPrice())),
            Map.of(Map.of("Iron", 5, "Coal", 1), new ArtisanProduct("Iron Bar", -1, 4, 10 * MineralType.Iron.getPrice())),
            Map.of(Map.of("Gold", 5, "Coal", 1), new ArtisanProduct("Gold Bar", -1, 4, 10 * MineralType.Gold.getPrice()))
    ));

    private final List<Map<Map<String, Integer>, ArtisanProduct>> items;
    public List<ArtisanProduct> products = new ArrayList<>();
    ArtisanType(List<Map<Map<String, Integer>, ArtisanProduct>> items) {


        this.items = items;

    }

    public CraftType getCraftType() {
        switch (this) {
            case BEE_HOUSE : {
                return CraftType.BeeHouse;
            }
            case CHEESE_PRESS : {
                return CraftType.CheesePress;
            }
            case Keg : {
                return CraftType.Keg;
            }
            case Dehydrator : {
                return null;
            }
            case CharcoalKlin : {
                return CraftType.CharcoalKlin;
            }
            case MayonnaiseMachine : {
                return CraftType.MayonnaiseMachine;
            }
            case OilMaker : {
                return CraftType.OilMaker;
            }
            case PreservesJar : {
                return CraftType.PreservesJar;
            }
            case FishSmoker : {
                return CraftType.FishSmoker;
            }
            case Furnace : {
                return CraftType.Furnace;
            }
        }
        return null;
    }

    public static ArtisanType getArtisan(String input) {
        input = input.replaceAll(" ", "").trim().toLowerCase();
        if (input.startsWith("beehouse")) {
            return BEE_HOUSE;
        } else if (input.startsWith("cheesepress")) {
            return CHEESE_PRESS;
        } else if (input.startsWith("keg")) {
            return Keg;
        } else if (input.startsWith("dehydrator")) {
            return Dehydrator;
        } else if (input.startsWith("charcoalklin")) {
            return CharcoalKlin;
        } else if (input.startsWith("loom")) {
            return Loom;
        } else if (input.startsWith("mayonnaisemachine")) {
            return MayonnaiseMachine;
        } else if (input.startsWith("oilmaker")) {
            return OilMaker;
        } else if (input.startsWith("preservesjar")) {
            return PreservesJar;
        } else if (input.startsWith("fishsmoker")) {
            return FishSmoker;
        } else if (input.startsWith("furnace")) {
            return Furnace;
        }
        return null;
    }

    private String getName() {
        switch (this) {
            case BEE_HOUSE : {
                return "beehouse";
            }
            case CHEESE_PRESS : {
                return "cheesepress";
            }
            case Keg : {
                return "keg";
            }
            case Dehydrator : {
                return "dehydrator";
            }
            case CharcoalKlin : {
                return "charcoalklin";
            }
            case MayonnaiseMachine : {
                return "mayonnaisemachine";
            }
            case OilMaker : {
                return "oilmaker";
            }
            case PreservesJar : {
                return "preservesjar";
            }
            case FishSmoker : {
                return "fishsmoker";
            }
            case Furnace : {
                return "furnace";
            }
        }
        return "";
    }

    public void useArtisan(String input) {
        input = input.toLowerCase().replaceAll(" ", "").replaceAll(getName(), "");
        for (Map<Map<String, Integer>, ArtisanProduct> recipes : items) {
            List<Map.Entry<Map<String, Integer>, ArtisanProduct>> sortedEntries = new ArrayList<>(recipes.entrySet());

            sortedEntries.sort((e1, e2) -> {
                String key1 = e1.getKey().keySet().iterator().next();
                String key2 = e2.getKey().keySet().iterator().next();
                return Integer.compare(key2.length(), key1.length()); // Descending
            });

            for (Map.Entry<Map<String, Integer>, ArtisanProduct> entry : sortedEntries) {
                String newInput = input;
                boolean found = true;
                boolean canAfford = true;

                for (Map.Entry<String, Integer> ingredient : entry.getKey().entrySet()) {
                    if (input.contains(ingredient.getKey().toLowerCase().replaceAll(" ", ""))) {

//                        if (Game.getCurrentPlayer().getItemQuantity(Game.getDatabase().getItem(ingredient.getKey())) < ingredient.getValue()) {
//                            canAfford = false;
//                        }

                        newInput = newInput.replaceAll(ingredient.getKey().toLowerCase().replaceAll(" ", ""), "");
                    } else {
                        found = false;
                        break;
                    }
                }

//                if (found && !canAfford) {
//                    System.out.println("not enough ingredients in inventory");
//                    return;
//                }
                if (found && newInput.isEmpty()) {
                    System.out.print(entry.getValue().getName() + " will be ready in " + entry.getValue().getProcessingTime() + " hours");
                    ArtisanProduct product = entry.getValue();
                    products.add(new ArtisanProduct(product.getName(), product.getEnergy(), product.getProcessingTime(), product.getPrice()));

                    return;
                }
                if (found && !newInput.isEmpty()) {
                    System.out.print("Extra items in input: " + input + ".");
                    return;
                }
            }
        }

        System.out.println("invalid items.");
    }

    private static Map<Map<String, Integer>, ArtisanProduct> createRecipe(
            Map<String, Integer> ingredients, ArtisanProduct product) {
        return Map.of(
                Objects.requireNonNull(ingredients, "Ingredients cannot be null"),
                Objects.requireNonNull(product, "Product cannot be null")
        );
    }

    public List<ArtisanProduct> getProducts() {
        List<ArtisanProduct> artisanProducts = new ArrayList<>();
        for (ArtisanProduct product : products) {
            if (product.isReady()) artisanProducts.add(product);
        }
        return artisanProducts;
    }
}
