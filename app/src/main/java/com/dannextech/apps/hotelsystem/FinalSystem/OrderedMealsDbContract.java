package com.dannextech.apps.hotelsystem.FinalSystem;

import android.provider.BaseColumns;

/**
 * Created by amoh on 9/26/2017.
 */

public class OrderedMealsDbContract {
    //To prevent someone from accidentally instanstiating this class we make the contructor private
    private OrderedMealsDbContract(){}

    //inner class defines the table contents
    public static class MealEntry implements BaseColumns{
        public static final String TABLE_NAME = "mealordered";
        public static final String COL_MEAL_NAME = "name";
        public static final String COL_MEAL_PRICE = "price";
        public static final String COL_MEAL_QUANTITY = "quantity";
        public static final String COL_MEAL_CATEGORY = "category";
    }
}
