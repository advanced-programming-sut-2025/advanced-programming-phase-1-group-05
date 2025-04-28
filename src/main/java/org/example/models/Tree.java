
package org.example.models;

import org.example.models.Enums.Season;
import org.example.models.Enums.TileType;

import java.util.Map;
import java.util.Random;

public class Tree implements Item{
        private final String name;
        private int price;
        private Map.Entry<Integer,Integer> coordinates;
        private final String seed;
        private final String growthStages;
        private boolean fullyGrown;
        private final int totalHarvestTime;
        private final FruitAndVegetable fruit;
        private final int fruitHarvestCycle;
        private final boolean isFruitEdible;
        private boolean isFruitGrown;
        private boolean growFruit;
        private final int fruitEnergy;
        private final Season season;
        private boolean protectedByScareCrow;

        public Tree(String name, int price, String seed, String growthStages, int totalHarvestTime, FruitAndVegetable fruit,
                    int fruitHarvestCycle, boolean isFruitEdible, int fruitEnergy, Season season) {
            this.name = name;
            this.price = price;
            this.seed = seed;
            this.growthStages = growthStages;
            this.totalHarvestTime = totalHarvestTime;
            this.fruit = fruit;
            this.fruitHarvestCycle = fruitHarvestCycle;
            this.isFruitEdible = isFruitEdible;
            this.fruitEnergy = fruitEnergy;
            this.season = season;
        }

        public void plant(Map.Entry<Integer,Integer> coordinates){
            this.coordinates = coordinates;
        }

        public void startFruitCycle(){
            if(fullyGrown){
                //start fruit cycle
            } else return;
        }

        public Result harvestFruit() {
            if(isFruitGrown){
                //harvest fruit
                return new Result(true, "");
            } else {
                return new Result(false, "No fruit ready to be harvested");
            }
        }

        public void thunderEffect() {
            GameMap map = Game.getGameMap();
            GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
            //turn to coal
            tile.setTileType(TileType.Coal);
            map.getTrees().remove(this);
        }

        public Result cutDownTree(){
            Random rand = new Random();
            int randomNum = rand.nextInt(2) + 1;
            Game.getCurrentPlayer().getBackPack().getInventory().put(new Seed(this.seed),randomNum);
            return new Result(true, "The tree was cut down, giving you " + randomNum + " of its seeds!");
        }

        public Map.Entry<Integer,Integer> getCoordinates(){
            return coordinates;
        }
        public String getSeed(){
            return seed;
        }

        @Override
        public String getName() {
            return name;
        }
        @Override
        public int getPrice() {
            return price;
        }
    @Override
    public void setCoordinates(Map.Entry<Integer, Integer> coordinates) {
        this.coordinates = coordinates;
    }


    }