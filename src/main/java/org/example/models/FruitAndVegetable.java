package org.example.models;

import org.example.GrowthStep;
import org.example.models.Enums.CropType;
import org.example.models.Enums.GrowthStage;
import org.example.models.Enums.Season;

import java.util.*;

public class FruitAndVegetable implements Item {
    private Map.Entry<Integer,Integer> coordinates;
    private final CropType type;
    private boolean fullyGrown;
    private boolean protectedByScareCrow;
    private boolean hasBeenWatered;
    private boolean hasBeenFertilized;
    private int age;
    private List<GrowthStep> growthTimeline;
    private int currentGrowthStage;
    private boolean isFullyGrown;


    public FruitAndVegetable(CropType type) {
        this.type = type;
        this.growthTimeline = new ArrayList<>();
        int stageIndex = 1;
        String[]growthStages = type.getStages().split("-");
        for(String gs : growthStages) {
            //i have no idea what im doing
            growthTimeline.add(new GrowthStep(GrowthStage.valueOf(String.valueOf(stageIndex)), Integer.parseInt(gs)));
        }
        this.age = 0;
    }

    public void showCropInformation(){
        System.out.println("crop name : " + type.getName());
        System.out.println("crop source : " + type.getSource());
        System.out.println("growth stages : " + type.getStages());
        System.out.println("total harvest time : " + type.getTotalHarvestTime());
        System.out.println("one time : " + type.isOneTime());
        if(type.getRegrowthTime() == 0)System.out.println("regrowth time : ");
        else System.out.println("regrowth time : " + type.getRegrowthTime());
        System.out.println("base sell price : " + type.getPrice());
        System.out.println("is edible : " + type.isEdible());
        System.out.println("base energy : " + type.getEnergy());
        System.out.printf("seasons : ");
        for(Season season : type.getSeasons()) {
            System.out.printf("%s ", season);
        }
        System.out.println();
        System.out.println("can become giant : " + type.canBecomeGiant());
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

    //mixed seed
    public FruitAndVegetable mixedSeedPlant(){
        Season currentSeason = new TimeAndDate().getCurrentSeason();
        ArrayList<FruitAndVegetable> possiblePlants = new ArrayList<>();
        FruitAndVegetable selectedPlant = null;
        //plants should be from another json file but for now
//        for(FruitAndVegetable plant : Game.getDatabase().getFruitAndVegetables()) {
//            for(Season season : plant.getSeasons()) {
//                if(season == currentSeason) {
//                    possiblePlants.add(plant);
//               }
//            }
//        }

        Random random = new Random();
        selectedPlant = possiblePlants.get(random.nextInt(possiblePlants.size()));
        return selectedPlant;
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

    public void plant(Map.Entry<Integer,Integer> coordinates){
        this.coordinates = coordinates;
    }
    public void waterCrop(){
        hasBeenWatered = true;
    }
    public void fertilize(){
        hasBeenFertilized = true;
    }

    public void grow(){
        if(hasBeenWatered && currentGrowthStage < growthTimeline.size()) {
            GrowthStep currentStage = growthTimeline.get(currentGrowthStage);
            age++;

            if(age > currentStage.getDays()) {
                currentGrowthStage++;
                if(currentGrowthStage >= growthTimeline.size()) {
                    isFullyGrown = true;
                }
            }
            hasBeenWatered = false;
        }
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