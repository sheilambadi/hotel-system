package com.dannextech.apps.hotelsystem.FinalSystem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amoh on 9/26/2017.
 */

public class OrderedMealsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OrderedMeals";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+
            OrderedMealsDbContract.MealEntry.TABLE_NAME + "(" +
            OrderedMealsDbContract.MealEntry._ID + " INTEGER PRIMARY KEY, " +
            OrderedMealsDbContract.MealEntry.COL_MEAL_NAME + " TEXT, "+
            OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE + " INT, "+
            OrderedMealsDbContract.MealEntry.COL_MEAL_QUANTITY + " INT, "+
            OrderedMealsDbContract.MealEntry.COL_MEAL_CATEGORY + " TEXT)";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "+ OrderedMealsDbContract.MealEntry.TABLE_NAME;
    public OrderedMealsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
