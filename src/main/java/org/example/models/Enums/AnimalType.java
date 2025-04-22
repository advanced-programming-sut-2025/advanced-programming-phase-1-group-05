package org.example.models.Enums;

import org.example.models.Product;

import java.util.List;

public enum AnimalType {
    Chicken("Chicken", org.example.models.Enums.CoopAndBarnType.Small, 800, List.of(new Product("egg", "", 50, -1))),
    Duck(),
    Rabbit(),
    Dinosaur(),
    Cow(),
    Goat(),
    Sheep(),
    Pig();


    private final String type;
    private final CoopAndBarnType CoopAndBarnType;
    private final int price;
    private final List<Product> produce;

    AnimalType(String type, CoopAndBarnType coopAndBarnType, int price, List<Product> produce) {
        this.type = type;
        this.CoopAndBarnType = coopAndBarnType;
        this.price = price;
        this.produce = produce;
    }
}