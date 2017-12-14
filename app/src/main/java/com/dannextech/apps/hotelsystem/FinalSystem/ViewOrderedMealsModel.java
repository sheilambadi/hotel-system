package com.dannextech.apps.hotelsystem.FinalSystem;

/**
 * Created by amoh on 10/10/2017.
 */

public class ViewOrderedMealsModel {
    private String Meal,Quantity;

    public ViewOrderedMealsModel(){

    }

    public String getMeal() {
        return Meal;
    }

    public void setMeal(String meal) {
        Meal = meal;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public ViewOrderedMealsModel(String meal, String quantity) {

        Meal = meal;
        Quantity = quantity;
    }
}
