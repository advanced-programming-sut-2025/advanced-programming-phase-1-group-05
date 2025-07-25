package org.example.models;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.example.models.Enums.TileType;

import java.util.ArrayList;
import java.util.List;

public class Store extends Actor {
    String storeName;
    List<Product> products = new ArrayList<>();
    int openingTime, closingTime;
    Texture tex;

    Store(String name, List<Product> products, int xStart, int xEnd,
          int yStart, int yEnd, int openingTime, int closingTime) {
        this.storeName = name;
        this.products.addAll(products);
        tex = GameAssetManager.getInstance().getOrLoadTexture("stores/" + storeName.toLowerCase().replaceAll("\\s+", "") + ".png");
        setBounds(xStart, yStart, tex.getWidth(), tex.getHeight());
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public boolean isInside(float x, float y) {
        return getBoundingRectangle().contains(x, y);
    }
    public boolean isOpen(int currentHour) {
        return currentHour >= openingTime && currentHour <= closingTime;
    }
    public String getStoreName() {
        return storeName;
    }
    public List<Product> getProducts() {
        return products;
    }
    public Product getProduct(String name) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
    }

    public boolean contains(Product product) {
        return products.contains(product);
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(tex, getX(), getY());
    }


    public Rectangle getBoundingRectangle() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }
}
