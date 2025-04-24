package org.example.models.Tool;

import org.example.models.*;
import org.example.models.Enums.ItemLevel;

import java.util.HashMap;

public class TrashCan implements Tool <ItemLevel> {
    ItemLevel level = ItemLevel.Normal;

    public void removeFromInventory(String name, int quantity, boolean flag){
        HashMap<Item, Integer> items = App.getCurrentPlayer().getBackPack().getInventory();
        for(Item item : items.keySet()){
            if(!flag) quantity = items.get(item);
            if(item.getName().equals(name)){
                //remove selected amount
                items.put(item, items.get(item) - quantity);
                //return a certain percent of the item's price
                Game.getCurrentPlayer().addGold((int) (item.getPrice()*level.getTrashcanCoeff()));
            }
        }
    }

    @Override
    public String getName() {
        return "Trash Can";
    }
    @Override
    public int getPrice() {
        return 1000;
    }
    @Override
    public void use(HashMap.Entry<Integer, Integer> coordinates) {
        //nada
    }
    @Override
    public void reduceEnergy(int amount){
        if(amount < 0) amount = 0;
        Game.getCurrentPlayer().addEnergy(-amount);
    }
    @Override
    public ItemLevel getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.nextLevel();
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }
}