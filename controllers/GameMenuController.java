package controllers;

import models.App;
import models.Item;
import models.Result;

import java.util.HashMap;

public class GameMenuController {
    public Result showCraftInfo(String name){

    }

    public Result newGame(String[] usernames){
        return null;
    }

    public Result chooseMap(int mapNumber){
        return null;
    }

    public Result loadGame(){
        return null;
    }

    public Result exitGame(){
        return null;
    }

    public Result nextTurn(){
        return null;
    }

    public Result deleteGame(){
        return null;
    }

    //for showing current player's energy level
    public Result showEnergy() {
        System.out.println("energy :" + App.getCurrentPlayer().getEnergy());
        return new Result(true, "");
    }

    //cheat code set energy
    public Result setEnergy(int value){
        App.getCurrentPlayer().addEnergy(value);
        return new Result(true, "** your energy got increased by " + value + " **");
    }

    //cheat code unlimited energy
    public Result unlimitedEnergy(){
        App.getCurrentPlayer().setUnlimitedEnergy();
        return new Result(true, "** your energy is unlimited now **");
    }

    //showing the items in inventory
    public Result showInventory(){
        HashMap<Item, Integer> items = App.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(items.get(item) != 0) System.out.println(items.get(item) + " of " + item.getName());
        }
        return new Result(true, "");
    }

    //removing from inventory
    public Result removeFromInventory(String name, int quantity, boolean flag){
        HashMap<Item, Integer> items = App.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(!flag) quantity = items.get(item);
            if(item.getName().equals(name)){
                //remove selected amount
                items.put(item, items.get(item) - quantity);
            }
        }
        return new Result(true, quantity + " of " + name + " was removed from inventory");
    }


}
