package org.example.Common;

public class InventorySlot {
    public float x,y;
    public Item item;
    public int count;

    boolean contains(float px, float py) {
        return px >= x && px <= x && py >= y && py <= y;
    }
}
