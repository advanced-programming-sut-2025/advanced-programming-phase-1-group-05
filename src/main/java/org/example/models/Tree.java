
package org.example.models;

import org.example.models.Enums.Season;

import java.util.Map;

    public class Tree implements Item{
        private final String name;
        private int price;
        private Map.Entry<Integer,Integer> coordinates;
        private final String seed;
        private final String growthStages;
        private final int totalHarvestTime;
        private final FruitAndVegetable fruit;
        private final int fruitHarvestCycle;
        private final boolean isFruitEdible;
        private final int fruitEnergy;
        private final Season season;

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

        @Override
        public String getName() {
            return name;
        }
        @Override
        public int getPrice() {
            return price;
        }

    }