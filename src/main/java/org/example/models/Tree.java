
package org.example.models;

import org.example.models.Enums.Season;
import org.example.models.Enums.TileType;
import org.example.models.Enums.TreeType;

import java.util.Map;
import java.util.Random;

public class Tree implements Item{
        private final TreeType treeType;
        private int price;
        private Map.Entry<Integer,Integer> coordinates;
        private boolean fullyGrown;
        private boolean isFruitGrown;
        private boolean growFruit;
        private boolean protectedByScareCrow;

        public Tree(TreeType treeType) {
            this.treeType = treeType;
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
            Game.getCurrentPlayer().getBackPack().getInventory().put(new Seed(this.treeType.getSeed()),randomNum);
            return new Result(true, "The tree was cut down, giving you " + randomNum + " of its seeds!");
        }

        public Map.Entry<Integer,Integer> getCoordinates(){
            return coordinates;
        }
        public TreeType getTreeType() {
            return treeType;
        }
        @Override
        public String getName() {
            return this.treeType.getName();
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