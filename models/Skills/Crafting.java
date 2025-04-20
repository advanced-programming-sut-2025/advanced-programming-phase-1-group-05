package models.Skills;

import models.GameTile;
import models.Result;

public class Crafting implements Skill{
    int level;

    public Result craftItem(String itemType, Inventory inventory) {}

    public void placeItemOnGround(String item, GameTile tile) {}

    public void addItemToInventory(String item, Inventory inventory) {}
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

}
