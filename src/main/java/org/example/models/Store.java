package org.example.models;

import org.example.models.Enums.StoreType;

import java.util.List;

public class Store {
    String storeName;
    NPC storeOwner;
    List<Product> products;

    Store(StoreType storeType) {
        this.storeName = storeType.getStoreName();
        this.storeOwner = null;
        this.products = storeType.getProducts();
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
}