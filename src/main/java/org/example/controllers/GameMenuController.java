package org.example.controllers;

import org.example.models.*;
import org.example.models.Tool.Tool;

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
        System.out.println("energy :" + Game.getCurrentPlayer().getEnergy());
        return new Result(true, "");
    }

    //cheat code set energy
    public Result setEnergy(int value){
        Game.getCurrentPlayer().addEnergy(value);
        return new Result(true, "** your energy got increased by " + value + " **");
    }

    //cheat code unlimited energy
    public Result unlimitedEnergy(){
        Game.getCurrentPlayer().setUnlimitedEnergy();
        return new Result(true, "** your energy is unlimited now **");
    }

    //showing the items in inventory
    public Result showInventory(){
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(items.get(item) != 0) System.out.println(items.get(item) + " of " + item.getName());
        }
        return new Result(true, "");
    }

    //removing from inventory
    public Result removeFromInventory(String name, int quantity, boolean flag){
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(!flag) quantity = items.get(item);
            if(item.getName().equals(name)){
                //remove selected amount
                items.put(item, items.get(item) - quantity);
            }
        }
        return new Result(true, quantity + " of " + name + " was removed from inventory");
    }

    //equip a certain tool
    public Result equipTool(String name){
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(item.getName().equals(name)){
                items.put(item, items.get(item) - 1);
                Game.getCurrentPlayer().setCurrentItem(item);
                return new Result(true, "You're now equipped with " + item.getName());
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }

    //showing current tool
    public Result showCurrentTool(){
        Item currentItem = Game.getCurrentPlayer().getCurrentItem();
        if(currentItem instanceof Tool) {
            return new Result(true, "You are equipped with " + currentItem.getName());
        } else {
            return new Result(true, "You are not equipped with any tool");
        }
    }

    //showing available tools
    public Result showAvailableTools(){
        boolean found = false;
        for(Item i : Game.getCurrentPlayer().getInventory().keySet()){
            if(i instanceof Tool) {
                System.out.println("* " + i.getName());
                found = true;
            }
        }
        if(!found) return new Result(true, "You don't have any tools in your inventory");
        return new Result(true, "");
    }

    //upgrade tool
    public Result upgradeTool(String name){
        //only available when in ahangary
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getInventory();
        for(Item item : items.keySet()){
            if(item.getName().equals(name)){
                if(item instanceof Tool) {
                    //reduce money
                    ((Tool) item).upgradeLevel();
                    return new Result(true, item.getName() + " upgraded to level" + ((Tool)item).getLevel());
                } else {
                    return new Result(true, "Selected item is not a tool");
                }
            }
        }
        return new Result(true, "You don't have that tool in your inventory");
    }



    public Result meetNPC(String npcName){
        NPC npc = Game.getNPCByName(npcName);


        return new Result(true, DialogueManager.getNpcDialogue(npcName, Game.getCurrentWeather().toString()));
    }


}