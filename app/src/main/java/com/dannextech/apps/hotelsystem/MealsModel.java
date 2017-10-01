package com.dannextech.apps.hotelsystem;

/**
 * Created by amoh on 9/25/2017.
 */

public class MealsModel {
    private String name, price,quantity;

    public MealsModel(String name, String price, String quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }
    public void setQuantity(String quantity){
        this.quantity = quantity;
    }
    public String getQuantity(){
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
