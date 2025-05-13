package org.example.models.Tool;

import org.example.models.*;
import org.example.models.Enums.ItemLevel;

import java.util.HashMap;
import java.util.Map;

public class TrashCan implements Tool <ItemLevel> {
    ItemLevel level = ItemLevel.Normal;

    public void removeFromInventory(String name, int quantity, boolean flag){
        HashMap<Item, Integer> items = Game.getCurrentPlayer().getBackPack().getInventory();
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

    public void setLevel(ItemLevel level){
        this.level = level;
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
    public Result use(HashMap.Entry<Integer, Integer> coordinates) {
        //nada
        return new Result(false, "");
    }
    @Override
    public boolean reduceEnergy(int amount){
        return true;
    }
    @Override
    public ItemLevel getLevel() {
        return level;
    }
    @Override
    public void upgradeLevel(){
        if (!level.isMaxLevel()) {
            level = level.upgradeLevel();
            System.out.println(getName() + " upgraded to " + level.getName());
        }
    }
}