package org.example.models;

import java.util.ArrayList;
import java.util.List;

public class Store {
    String storeName;
    NPC storeOwner;
    List<Product> products = new ArrayList<>();
    int xStart, xEnd, yStart, yEnd;
    int openingTime, closingTime;

    Store(String name, List<Product> products, int xStart, int xEnd,
          int yStart, int yEnd, int openingTime, int closingTime) {
        this.storeName = name;
        this.products.addAll(products);
        this.xStart = xStart;
        this.xEnd = xEnd;
        this.yStart = yStart;
        this.yEnd = yEnd;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public boolean isInside(int x, int y) {
        return x >= xStart && x <= xEnd && y >= yStart && y <= yEnd;
    }
    public boolean isOpen(int currentHour) {
        return currentHour >= openingTime && currentHour <= closingTime;
    }
    public String getStoreName() {
        return storeName;
    }
    public NPC getStoreOwner() {
        return storeOwner;
    }
    public List<Product> getProducts() {
        return products;
    }
    public Product getProduct(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public boolean contains(Product product) {
        return products.contains(product);
    }
}