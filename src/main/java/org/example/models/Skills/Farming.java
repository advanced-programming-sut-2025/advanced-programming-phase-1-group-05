package org.example.models.Skills;

import org.example.models.*;
import org.example.models.Enums.*;
import org.example.models.Tool.Hoe;
import org.example.models.Tool.Scythe;
import org.example.models.Tool.WateringCan;

import java.util.*;

public class Farming implements Skill{
    int level = 0;
    int capacity = 0;

    public void plowTile(GameTile tile, Hoe hoe) {
        tile.setTileType(TileType.Soil);
    }

    //plant seed
    public boolean plantSeed(String seed, GameTile tile) {
        CropType cropType = CropType.getSeedType(seed);
        TreeType treeType = TreeType.getSeedType(seed);
        if(seed.equals("Mixed Seeds")) {
            FruitAndVegetable newCrop = mixedSeedPlant();
            tile.setItemOnTile(newCrop);
            Game.getGameMap().addPlant(newCrop);
            //can become giant
            if(newCrop.canBecomeGiant(Game.getCurrentPlayer().getFarm().getCrops())) {
                newCrop.expandToGiant(Game.getCurrentPlayer().getFarm().getCrops());
            }
        }
        if(cropType != null) {
            FruitAndVegetable crop = new FruitAndVegetable(cropType);
            tile.setItemOnTile(crop);
            Game.getGameMap().addPlant(crop);
            if(tile.hasBeenFertilized()) {
                crop.fertilize(tile.getFertilizer());
            }
            if(crop.canBecomeGiant(Game.getCurrentPlayer().getFarm().getCrops())) {
                crop.expandToGiant(Game.getCurrentPlayer().getFarm().getCrops());
            }
            return true;
        } else if (treeType != null) {
            Tree tree = new Tree(treeType);
            tile.setItemOnTile(tree);
            Game.getGameMap().addTree(tree);
            return true;
        }
        return false;
    }

    //mixed seed
    public FruitAndVegetable mixedSeedPlant(){
        Season currentSeason = new TimeAndDate().getCurrentSeason();
        List<CropType> possiblePlants = PossibleSeed.getPossibleSeeds(currentSeason);

        Random random = new Random();
        CropType selectedType = possiblePlants.get(random.nextInt(possiblePlants.size()));
        return new FruitAndVegetable(selectedType);
    }

    //fertilize crop
    public boolean fertilizeCrop(Map.Entry<Integer, Integer> coordinantes, String fertilizer) {
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getBackPack().getInventory();
        for(Item item : items.keySet()) {
            if(item.getName().equals(fertilizer)) {
              //  FruitAndVegetable fruit = Game.getGameMap().getPlantedFruit(coordinantes);
                // idk the details
               // fruit.fertilize();
                return true;
            }
        }
        return false;
    }

    //water crop
    public void waterCrop(FruitAndVegetable crop) {
        crop.waterCrop();
    }

    //harvest crop
    public void harvestCrop(GameTile tile) {
        Item item = tile.getItemOnTile();
        if(item instanceof FruitAndVegetable) {
            Game.getCurrentPlayer().getBackPack().addToInventory(tile.getItemOnTile(), 1);
            if (((FruitAndVegetable) item).isOneTime()) tile.setItemOnTile(null);
            ((FruitAndVegetable) item).setHarvested(true);
        } else if(item instanceof Tree) {
            String fruitName = ((Tree) item).getTreeType().getFruit();
            int fruitPrice = ((Tree) item).getTreeType().getFruitPrice();
            Item fruit = new BasicItem(fruitName, fruitPrice);
            Random rand = new Random();
            int randomNum = rand.nextInt(5);
            Game.getCurrentPlayer().getBackPack().addToInventory(fruit, randomNum);
            ((Tree)item).harvestFruit();
        }
        increaseCapacity();
    }


    @Override
    public int getLevel() {
        return level;
    }
    @Override
    public boolean isMaxLevel() {
        return level == 10;
    }
    @Override
    public void setLevel(int level) {
        this.level = level;
    }
    @Override
    public void increaseLevel() {
        if(level < 4) this.level ++;
        handleLevelChangeTrophies(level);
    }
    @Override
    public boolean canGoToNextLevel() {
        if((level + 1) * 100 + 50 <= capacity) {
            capacity -= (level + 1)*100 + 50;
            increaseLevel();
            return true;
        }
        return false;
    }
    @Override
    public void increaseCapacity() {
        this.capacity += 5;
    }
    @Override
    public void handleLevelChangeTrophies(int level){
        switch(level) {
            case 1: {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Sprinkler);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.BeeHouse);
                break;
            }
            case 2: {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.QualitySprinkler);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.DeluxeScareCrow);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.CheesePress);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.PreservesJar);
                break;
            }
            case 3: {
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.IridiumSprinkler);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Keg);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.Loom);
                Game.getCurrentPlayer().getBackPack().addLearntRecipe(CraftType.OilMaker);
                break;
            }
        }
    }
}