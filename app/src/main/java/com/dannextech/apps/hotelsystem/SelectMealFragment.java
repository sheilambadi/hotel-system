package com.dannextech.apps.hotelsystem;


import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectMealFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btMeals, btDrinks, btSnacks, btSubmitMeals;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myDBRef;

    OrderedMealsDbHelper dbHelper;


    MealsModel[] mealsModelFoods= {new MealsModel("Githeri","100","1"),new MealsModel("Beef and Ugali","200","1"), new MealsModel("Fish and Ugali","250","1"),new MealsModel("Chicken and Rice","400","1")};;
    MealsModel[] mealsModelDrinks = {new MealsModel("Tea","50","1"),new MealsModel("Coffee","70","1"),new MealsModel("Soda","70","1"),new MealsModel("Juice","100","1")};
    MealsModel[] mealsModelSnacks = {new MealsModel("Chips","100","1"),new MealsModel("Sausage","50","1"),new MealsModel("Kebab","50","1"),new MealsModel("Samosa","50","1"),new MealsModel("cake","50","1")};

    String category = "Food";

    public SelectMealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_select_meal, container, false);
        //Step 1: Get a reference to the recycleview
        recyclerView = (RecyclerView) rootView.findViewById(R.id.mealList);
        btDrinks = (Button) rootView.findViewById(R.id.btDrinks);
        btMeals = (Button) rootView.findViewById(R.id.btMeals);
        btSnacks = (Button) rootView.findViewById(R.id.btSnacks);
        btSubmitMeals = (Button) rootView.findViewById(R.id.btSubmitMeals);

        dbHelper = new OrderedMealsDbHelper(getContext());
        //use this setting to improve performance if you know that changes in content do not change the layout size
        recyclerView.setHasFixedSize(true);
        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.

        //Step 2: Set layout manager
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //Step 3: create an adapter
        MealAdapter adapter = new MealAdapter(mealsModelFoods,category);
        //Step 4: set the adapater
        recyclerView.setAdapter(adapter);
        //Step 5: Set item animator to defaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        btDrinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Drink";
                //recyclerView.setLayoutManager(layoutManager);
                //Step 3: create an adapter
                MealAdapter adapter = new MealAdapter(mealsModelDrinks,category);
                //Step 4: set the adapater
                recyclerView.setAdapter(adapter);
                //Step 5: Set item animator to defaultAnimator
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
        btSnacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Snack";
                //recyclerView.setLayoutManager(layoutManager);
                //Step 3: create an adapter
                MealAdapter adapter = new MealAdapter(mealsModelSnacks,category);
                //Step 4: set the adapater
                recyclerView.setAdapter(adapter);
                //Step 5: Set item animator to defaultAnimator
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });
        btMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                category = "Food";
                //recyclerView.setLayoutManager(layoutManager);
                //Step 3: create an adapter
                MealAdapter adapter = new MealAdapter(mealsModelFoods,category);
                //Step 4: set the adapater
                recyclerView.setAdapter(adapter);
                //Step 5: Set item animator to defaultAnimator
                recyclerView.setItemAnimator(new DefaultItemAnimator());
            }
        });

        btSubmitMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tableName = selectTable();


            }
        });
        return rootView;
    }

    private void selectMeal(final String tableName) {
        final SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Define a project that specifies which columns from the database you will actually use after this query
        String[] projection = {OrderedMealsDbContract.MealEntry.COL_MEAL_NAME, OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE, OrderedMealsDbContract.MealEntry.COL_MEAL_QUANTITY, OrderedMealsDbContract.MealEntry.COL_MEAL_CATEGORY};

        //Filter results where MealName = Tea
       // String selection = OrderedMealsDbContract.MealEntry.COL_MEAL_NAME+ " = ?";
        //String[] selectionArgs = {"tea"};

        //How you want to sort the result in the resulting cursor
       // String sortOrder = OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE + " DESC";

        Cursor cursor = db.query(
                OrderedMealsDbContract.MealEntry.TABLE_NAME,        //The table to query
                projection,                                         //The columns to return
                null,                                               //The columns for the where clause eg: selection
                null,                                               //The values for the where clause  eg: selectionArgs
                null,                                               //How to group the rows
                null,                                               //How to filter by row groups
                null                                           //The sort order
        );
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getContext());
        //builderSingle.setIcon(R.drawable.menu);


        final ArrayAdapter<String> mealArrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1);

        builderSingle.setTitle("Confirm Selection for "+tableName);

        int i=0;
        final String[] prices = new String[100];
        final String[] categories = new String[100];
        final String[] quantities = new String[100];
        while (cursor.moveToNext()){
            mealArrayAdapter.add(cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_NAME)));
            prices[i] = cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE));
            categories[i] = cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_CATEGORY));
            quantities[i] = cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_QUANTITY));
            Toast.makeText(getContext(),cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_NAME))+" is "+prices[i]+" and "+quantities[i],Toast.LENGTH_SHORT).show();
            i++;
        }
        builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectTable();
            }
        });
        builderSingle.setAdapter(mealArrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getContext(),"Ordered meals listed",Toast.LENGTH_SHORT).show();
            }
        });
        builderSingle.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                uploadSelectedMeals(mealArrayAdapter,tableName,prices,categories,quantities);
                db.execSQL("delete from " + OrderedMealsDbContract.MealEntry.TABLE_NAME);
                Toast.makeText(getContext(),"You have successfully submitted your order",Toast.LENGTH_SHORT).show();
            }
        });
        builderSingle.show();
        cursor.close();
    }

    private void uploadSelectedMeals(ArrayAdapter<String> arrayAdapter, String tableName, String[] prices, String[] categories, String[] quantities) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String timeNow = sdf.format(currentTime);
        String paymentState = "Not Paid";
        String serviceState = "Not Served";

        mDatabase = FirebaseDatabase.getInstance();
        myDBRef = mDatabase.getReference().child("Ordered Meals").push();

        DatabaseReference table = myDBRef.child("Table Name");
        DatabaseReference time = myDBRef.child("Time Ordered");
        DatabaseReference paymentStatus = myDBRef.child("Payment Status");
        DatabaseReference serviceStatus = myDBRef.child("Service Status");


        table.setValue(tableName);
        time.setValue(timeNow);
        paymentStatus.setValue(paymentState);
        serviceStatus.setValue(serviceState);

        for(int i=0; i<arrayAdapter.getCount();i++){
            DatabaseReference mealsOdered = myDBRef.child("Meals Ordered").push();

            DatabaseReference mealName = mealsOdered.child("Meal");
            DatabaseReference mealCategory = mealsOdered.child("Category");
            DatabaseReference mealPrice = mealsOdered.child("Price");
            DatabaseReference mealQuantity = mealsOdered.child("Quantity");

            mealName.setValue(arrayAdapter.getItem(i).toString());
            mealPrice.setValue(prices[i]);
            mealCategory.setValue(categories[i]);
            mealQuantity.setValue(quantities[i]);

        }
    }

    private String selectTable() {
        final String[] tableName = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Select Table");
        final ArrayAdapter<String> tableAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_singlechoice);
        tableAdapter.add("Table 1");
        tableAdapter.add("Table 2");
        tableAdapter.add("Table 3");
        tableAdapter.add("Table 4");
        tableAdapter.add("Table 5");
        tableAdapter.add("Table 6");
        tableAdapter.add("Table 7");
        tableAdapter.add("Table 8");
        tableAdapter.add("Table 9");
        tableAdapter.add("Table 10");

        builder.setAdapter(tableAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                tableName[0] = (tableAdapter.getItem(i));
                selectMeal(tableName[0]);

            }
        });
        builder.show();
        return tableName[0];
    }
}
