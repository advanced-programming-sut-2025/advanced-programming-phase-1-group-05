package org.example.controllers;

import org.example.models.*;

import java.util.ArrayList;
import java.util.List;

public class StoreController {


//    //for showing all the products in a store
//    public Result showAllProducts(Store store){
//        List<Product> products = store.getProducts();
//        for(Product product : products) {
//            //show product name and price
//        }
//        return null;
//    }
//
//    //for showing all available products in a store
//    public Result showAvailableProducts(Store store) {
//        List<Product> products = store.getProducts();
//        for (Product product : products) {
//            if (product.isAvailable()) {
//                //show product name and price
//            }
//        }
//        return null;
//    }
    private Store getCurrentStore() {
        Player player = Game.getCurrentPlayer();
        for (Store store : Game.getDatabase().getStores()){
            if (store.isInside(player.getX(), player.getY())){
                return store;
            }
        }
        return null;
    }
    //for purchasing a product
    public Result purchase(String productName, int count) {
        Store store = getCurrentStore();
        if (store == null) {
            return Result.error(" ");
        }
        Product product = store.getProduct(productName);

        if (product == null) {
            return Result.error(" ");
        }
        //sell item
        if(product.getLimit() < count) {
            //can't purchase
        } else if (Game.getCurrentPlayer().getGold() < product.getPrice()){
            //not enough gold
        }
        return null;
    }
//
//    //cheat code for increasing ??
//    public Result addDollars(int amount) {
//
//        return null;
//    }




}