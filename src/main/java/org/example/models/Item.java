package org.example.models;

import org.example.models.Enums.Material;

import java.util.Map;

public interface Item extends Material {
    String getName();
    int getPrice();
}