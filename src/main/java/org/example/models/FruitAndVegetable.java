package org.example.models;

import org.example.GrowthStep;
import org.example.models.Enums.CropType;
import org.example.models.Enums.GrowthStage;
import org.example.models.Enums.Season;

import java.util.*;

public class FruitAndVegetable implements Item {
    private Map.Entry<Integer,Integer> coordinates;
    private final CropType type;
    private boolean protectedByScareCrow;
    private boolean hasBeenWatered;
    private boolean hasBeenFertilized;
    private boolean isSpeedGro;
    private boolean isRetainingSoil;
    private int age;
    private List<GrowthStep> growthTimeline;
    private int currentGrowthStage;
    private boolean isFullyGrown;
    private boolean isHarvested;
    private int regrowthCounter;
    private int daysNoWater;
    private boolean alive = true;


    public FruitAndVegetable(CropType type) {
        this.type = type;
        this.growthTimeline = new ArrayList<>();
        int stageIndex = 1;
        String[]growthStages = type.getStages().split("-");
        for(String gs : growthStages) {
            growthTimeline.add(new GrowthStep(GrowthStage.valueOf(String.valueOf(stageIndex)), Integer.parseInt(gs)));
            stageIndex++;
        }
        this.age = 0;
        this.regrowthCounter = 0;
    }

    public boolean isFullyGrown() {
        return isFullyGrown;
    }
    public Map.Entry<Integer,Integer> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(Map.Entry<Integer,Integer> coordinates) {
        this.coordinates = coordinates;}

    //when planting a fruit or vegetable
    public boolean canBecomeGiant(List<FruitAndVegetable> plants) {
        if(!type.canBecomeGiant()) return false;
        int x = coordinates.getKey();
        int y = coordinates.getValue();
        int adjacentCount = 0;

        for (FruitAndVegetable p : plants) {
            int otherX = p.getCoordinates().getKey();
            int otherY = p.getCoordinates().getValue();

            if ((Math.abs(otherX - x) == 1 && otherY == y) || (Math.abs(otherY - y) == 1 && otherX == x)) {
                adjacentCount++;
            }
        }

        return true;
    }


    public void expandToGiant(List<FruitAndVegetable> plants) {
        if (type.canBecomeGiant() && canBecomeGiant(plants)) {
            int x = coordinates.getKey();
            int y = coordinates.getValue();

            setCoordinates(new AbstractMap.SimpleEntry<>(x, y));

        }
    }

    public boolean isProtectedByScareCrow() {
        return protectedByScareCrow;
    }
    public String getSeed(){
        return type.getSource();
    }
    public String getGrowthStage(){
        return type.getStages();
    }
    public int getTotalHarvestTime(){
        return type.getTotalHarvestTime();
    }
    public int getRegrowthTime(){
        return type.getRegrowthTime();
    }
    public boolean isOneTime(){
        return type.isOneTime();
    }
    public int getEnergy(){
        return type.getEnergy();
    }
    public List<Season> getSeasons(){
        return type.getSeasons();
    }

    public void waterCrop(){
        hasBeenWatered = true;
    }
    public void fertilize(String fertilizer) {
        hasBeenFertilized = true;
        if(fertilizer.equals("Speed-Gro")) {
            isSpeedGro = true;
        } else if(fertilizer.equals("Retaining-Soil")) {
            isRetainingSoil = true;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getAge(){
        return age;
    }
    public void grow(){
        if(!hasBeenWatered) daysNoWater++;
        if(daysNoWater == 2) alive = false;
        if(hasBeenWatered && currentGrowthStage < growthTimeline.size()) {
            GrowthStep currentStage = growthTimeline.get(currentGrowthStage);
            if(hasBeenFertilized) {
                if(isSpeedGro) {
                    age++;
                }
            }
            age++;

            if(age > currentStage.getDays()) {
                currentGrowthStage++;
                if(currentGrowthStage >= growthTimeline.size()) {
                    isFullyGrown = true;
                }
            } else if (hasBeenWatered && isHarvested && getRegrowthTime() != 0) {
                regrowthCounter++;
                if (regrowthCounter >= getRegrowthTime()) {
                    isHarvested = false;
                    isFullyGrown = true;
                    regrowthCounter = 0;
                }
            }

            hasBeenWatered = hasBeenFertilized && isRetainingSoil;
        }
    }

    public void setProtectedByScareCrow(boolean protectedByScareCrow) {
        this.protectedByScareCrow = protectedByScareCrow;
    }
    public void setHarvested(boolean harvested){
        isHarvested = harvested;
    }
    @Override
    public String getName() {
        return type.getName();
    }

    @Override
    public int getPrice() {
        return type.getPrice();
    }
    @Override
    public String toString() {
        return "Name: " + type.getName() + "\n" + "Seed: " + type.getSource() + "\n" +"Growth stage: " + type.getStages() + "\n" +
                "Has been watered today: " + hasBeenWatered +"\n" + "Has been fertilized: " + hasBeenFertilized;
    }
}