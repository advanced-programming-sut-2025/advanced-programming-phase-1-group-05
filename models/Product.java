package models;

public class Product extends Item{
    private final String name;
    private final String description;
    private final int price;
    private final int limit;
    private boolean available;

    public Product(String name, String description, int price, int limit) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.limit = limit;
        this.available = true;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public int getPrice() {
        return price;
    }
    public int getLimit() {
        return limit;
    }
    public boolean isAvailable() {
        return available;
    }
    public void setAvailable(boolean available) {
        this.available = available;
    }
}
