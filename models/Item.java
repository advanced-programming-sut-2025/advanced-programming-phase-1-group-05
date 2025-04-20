package models;

public class Item {
    //unique item stuff
    String name;
    int price;
    TimeAndDate producingTime;
    public Item(String name, int price, TimeAndDate producingTime) {
        this.name = name;
        this.price = price;
        this.producingTime = producingTime;
    }
    public String getName() {
        return name;
    }
    public int getPrice() {
        return price;
    }
    public TimeAndDate getProducingTime() {
        return producingTime;
    }
}
