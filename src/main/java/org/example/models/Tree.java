
package org.example.models;

import org.example.GrowthStep;
import org.example.models.Enums.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Tree implements Item{
        private final TreeType treeType;
        private Map.Entry<Integer,Integer> coordinates;
        private boolean fullyGrown;
        private boolean isFruitGrown;
        private boolean protectedByScareCrow;
        private final List<GrowthStep> treeGrowthTimeLine = new ArrayList<>();
        private int fruitGrowthCounter;
        private int treeAge;
        private int currentGrowthStep;
        private boolean isHarvested;

        public Tree(TreeType treeType) {
            this.treeType = treeType;
            int stageIndex =1;
            String[]growthStages = treeType.getStages().split("-");
            for(String gs : growthStages) {
                treeGrowthTimeLine.add(new GrowthStep(Integer.parseInt(gs)));
                stageIndex++;
            }
        }

        public boolean isProtectedByScareCrow(){
            return protectedByScareCrow;
        }

        public void growTree(){
            if(!fullyGrown) {
                treeAge++;
                if (currentGrowthStep < treeGrowthTimeLine.size()) {
                    GrowthStep currentStage = treeGrowthTimeLine.get(currentGrowthStep);
                    if (treeAge > currentStage.getDays()) {
                        currentGrowthStep++;
                        if (currentGrowthStep >= treeGrowthTimeLine.size()) {
                            fullyGrown = true;
                        }
                    }
                }
            } else {
                if (!isFruitGrown) startFruitCycle();
            }
        }

        public void startFruitCycle(){
            fruitGrowthCounter++;
            if(fruitGrowthCounter >= treeType.getFruitHarvestCycle()) {
                isFruitGrown = true;
                fruitGrowthCounter = 0;
            }
        }

        public Result harvestFruit() {
            if(isFruitGrown){
                isHarvested = true;
                isFruitGrown = false;
                fruitGrowthCounter = 0;
                return new Result(true, "Successfully harvested from the tree");
            } else {
                return new Result(false, "No fruit ready to be harvested");
            }
        }
        public ForagingItem getHarvestedFruit(){
            return new ForagingItem(ForagingCrop.fromString(treeType.getFruit()), treeType.getFruit(), treeType.getFruitPrice());
        }

        public void thunderEffect() {
            GameMap map = Game.getGameMap();
            GameTile tile = map.getTile(coordinates.getKey(), coordinates.getValue());
            tile.setItemOnTile(new BasicItem(MineralType.Coal.getName(), MineralType.Coal.getPrice()));
            map.getTrees().remove(this);
        }

        public Result cutDownTree(){
            Random rand = new Random();
            int randomNum = rand.nextInt(2) + 1;
            Game.getCurrentPlayer().getForagingSkill().cutDownTree();
            Game.getCurrentPlayer().getBackPack().getInventory().put(new BasicItem(treeType.getSeed(), 0),randomNum);
            return new Result(true, "The tree was cut down, giving you " + randomNum + " of its seeds!");
        }
        public void setProtectedByScareCrow(boolean protectedByScareCrow) {
            this.protectedByScareCrow = protectedByScareCrow;
        }
        @Override
        public String toString() {
            StringBuilder str = new StringBuilder();
            str.append("Name : ").append(treeType.getName()).append("\n")
                    .append("Fruit Name : ").append(treeType.getFruit()).append("\n")
                    .append("Tree Age : ").append(treeAge).append("\n")
                    .append("Tree Growth Stages :").append(treeType.getStages()).append("\n")
                    .append("Current Growth Step : ").append(currentGrowthStep).append("\n")
                    .append("Is tree grown : ").append(fullyGrown).append("\n")
                    .append("Is fruit grown : ").append(isFruitGrown).append("\n");
                    if(!isFruitGrown)str.append("Days left for harvest : ").append(treeType.getFruitHarvestCycle() - fruitGrowthCounter).append("\n");

            return str.toString();

        }

        public Map.Entry<Integer,Integer> getCoordinates(){
            return coordinates;
        }
        public TreeType getTreeType() {
            return treeType;
        }
        public void setFruitGrowthCounter(int fruitGrowthCounter) {
            this.fruitGrowthCounter = fruitGrowthCounter;
        }
        public void setFullyGrown(){
            this.fullyGrown = true;
        }
        @Override
        public String getName() {
            return this.treeType.getName();
        }
        @Override
        public int getPrice() {
            return 0;
        }
        public void setCoordinates(Map.Entry<Integer,Integer> coordinates) {
            this.coordinates = coordinates;
        }


    }