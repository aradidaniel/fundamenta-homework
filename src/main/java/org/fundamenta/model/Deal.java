package org.fundamenta.model;

public class Deal {

    String type;

    int price;

    public Deal(String type, int price) {
        this.type = type;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}
