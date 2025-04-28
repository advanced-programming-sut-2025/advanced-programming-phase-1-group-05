package org.example.models;

import java.util.Map;

public interface Item {
    String getName();
    int getPrice();
    void setCoordinates(Map.Entry<Integer, Integer> coordinates);
    Map.Entry<Integer, Integer> getCoordinates();
}