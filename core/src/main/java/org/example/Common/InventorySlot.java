package org.example.Common;

import java.io.Serializable;

public class InventorySlot   implements Serializable {
    public float x,y;
    public Item item;
    public int count;

    boolean contains(float px, float py) {
        return px >= x && px <= x && py >= y && py <= y;
    }
}
