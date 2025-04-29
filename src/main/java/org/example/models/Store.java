package org.example.models;

import org.example.models.Enums.StoreType;

import java.util.List;

public class Store {
    String storeName;
    NPC storeOwner;
    List<Product> products;
    int xStart, xEnd, yStart, yEnd;

    Store(StoreType storeType) {
        this.storeName = storeType.getStoreName();
        this.storeOwner = null;
        this.products = storeType.getProducts();
    }

    public boolean isInside(int x, int y) {
        return x >= xStart && x <= xEnd && y >= yStart && y <= yEnd;
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
}