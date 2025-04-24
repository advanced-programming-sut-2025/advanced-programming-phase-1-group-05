package org.example.models;

import org.example.models.Enums.Season;

import java.util.*;

public class FruitAndVegetable implements Item {
    private Map.Entry<Integer,Integer> coordinates;
    private final String name;
    private int price;

    private final String seed;
    private final String growthStage;
    private boolean fullyGrown;
    private final int totalHarvestTime;
    private final boolean oneTime;
    private final int regrowthTime;
    private final boolean isEdible;
    private final int energy;
    private List<Season> seasons = new ArrayList<>();
    private final boolean canBecomeGiant;
    private boolean protectedByScareCrow;
    private boolean hasBeenWatered;
    private boolean hasBeenFertilized;


    public FruitAndVegetable(String name, int price, String seed, String growthStage, int totalHarvestTime, boolean oneTime, int regrowthTime, boolean isEdible,
                             int energy, List<Season> seasons, boolean isGiantifiable) {
        this.name = name;
        this.price = price;
        this.seed = seed;
        this.growthStage = growthStage;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.isEdible = isEdible;
        this.energy = energy;
        this.seasons = seasons;
        this.canBecomeGiant = isGiantifiable;
    }

    public void showCropInformation(){
        System.out.println("crop name : " + name);
        System.out.println("crop source : " + seed);
        System.out.println("growth stages : " + growthStage);
        System.out.println("total harvest time : " + totalHarvestTime);
        System.out.println("one time : " + oneTime);
        if(regrowthTime == 0)System.out.println("regrowth time : ");
        else System.out.println("regrowth time : " + regrowthTime);
        System.out.println("base sell price : " + price);
        System.out.println("is edible : " + isEdible);
        System.out.println("base energy : " + energy);
        System.out.printf("seasons : ");
        for(Season season : seasons){
            System.out.printf("%s ", season);
        }
        System.out.println();
        System.out.println("can become giant : " + canBecomeGiant);
    }

    public Map.Entry<Integer,Integer> getCoordinates() {
        return coordinates;
    }
    public void setCoordinates(Map.Entry<Integer,Integer> coordinates) {
        this.coordinates = coordinates;}

    //when planting a fruit or vegetable
    public boolean canBecomeGiant(List<FruitAndVegetable> plants) {
        if(!canBecomeGiant) return false;
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
        if (canBecomeGiant && canBecomeGiant(plants)) {
            int x = coordinates.getKey();
            int y = coordinates.getValue();

            setCoordinates(new AbstractMap.SimpleEntry<>(x, y));

        }
    }

    //mixed seed
    public FruitAndVegetable mixedSeedPlant(){
        Season currentSeason = new TimeAndDate().getCurrentSeason();
        ArrayList<FruitAndVegetable> possiblePlants = new ArrayList<>();
        FruitAndVegetable selectedPlant = null;
        //plants should be from another json file but for now
        for(FruitAndVegetable plant : Game.getDatabase().getFruitAndVegetables()) {
            for(Season season : plant.getSeasons()) {
                if(season == currentSeason) {
                    possiblePlants.add(plant);
               }
            }
        }

        Random random = new Random();
        selectedPlant = possiblePlants.get(random.nextInt(possiblePlants.size()));
        return selectedPlant;
    }

    public String getSeed(){
        return seed;
    }
    public String getGrowthStage(){
        return growthStage;
    }
    public int getTotalHarvestTime(){
        return totalHarvestTime;
    }
    public int getRegrowthTime(){
        return regrowthTime;
    }
    public boolean isOneTime(){
        return oneTime;
    }
    public int getEnergy(){
        return energy;
    }
    public List<Season> getSeasons(){
        return seasons;
    }

    public void plant(Map.Entry<Integer,Integer> coordinates){
        this.coordinates = coordinates;
    }

    public void fertilize(){
        hasBeenFertilized = true;
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
    public String toString() {
        return "Name: " + name + "\n" + "Seed: " + seed + "\n" +"Growth stage: " + growthStage + "\n" +
                "Has been watered today: " + hasBeenWatered +"\n" + "Has been fertilized: " + hasBeenFertilized;
    }
}