package org.example.controllers;

import org.example.models.Game;
import org.example.models.Result;
import org.example.models.Store;
import org.example.models.Product;

import java.util.List;

public class StoreController {

    //for showing all the products in a store
    public Result showAllProducts(Store store){
        List<Product> products = store.getProducts();
        for(Product product : products) {
            //show product name and price
        }
        return null;
    }

    //for showing all available products in a store
    public Result showAvailableProducts(Store store) {
        List<Product> products = store.getProducts();
        for (Product product : products) {
            if (product.isAvailable()) {
                //show product name and price
            }
        }
        return null;
    }

    //for purchasing a product
    public Result purchase(String productName, Store store, int count) {
        List<Product> products = store.getProducts();
        Product productToPurchase = null;
        for (Product product : products) {
            if (product.getName().equals(productName)) {
                productToPurchase = product;
            }
        }

        //sell item
        if(productToPurchase.getLimit() < count) {
            //can't purchase
        } else if (Game.getCurrentPlayer().getGold() < productToPurchase.getPrice()){
            //not enough gold
        }
        return null;
    }

    //cheat code for increasing ??
    public Result addDollars(int amount) {

        return null;
    }




}