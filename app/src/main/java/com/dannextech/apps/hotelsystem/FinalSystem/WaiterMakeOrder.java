package com.dannextech.apps.hotelsystem.FinalSystem;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.dannextech.apps.hotelsystem.MealAdapter;
import com.dannextech.apps.hotelsystem.MealsModel;
import com.dannextech.apps.hotelsystem.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterMakeOrder extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button btMeals, btDrinks, btSnacks, btSubmitMeals;
    ProgressDialog progressDialog;

    private FirebaseDatabase mDatabase;
    private DatabaseReference myDBRef;

    OrderedMealsDbHelper dbHelper;

    SQLiteDatabase db;


    MealsModel[] mealsModelFoods= {new MealsModel("Githeri","100","1"),new MealsModel("Beef and Ugali","200","1"), new MealsModel("Fish and Ugali","250","1"),new MealsModel("Chicken and Rice","400","1")};;
    MealsModel[] mealsModelDrinks = {new MealsModel("Tea","50","1"),new MealsModel("Coffee","70","1"),new MealsModel("Soda","70","1"),new MealsModel("Juice","100","1")};
    MealsModel[] mealsModelSnacks = {new MealsModel("Chips","100","1"),new MealsModel("Sausage","50","1"),new MealsModel("Kebab","50","1"),new MealsModel("Samosa","50","1"),new MealsModel("cake","50","1")};

    String category = "Food";

    public WaiterMakeOrder() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (isNetworkAvailable()){
            Toast.makeText(getContext(),"Network is "+ isNetworkAvailable(),Toast.LENGTH_SHORT).show();
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
                    if(isNetworkAvailable()){
                        String tableName = selectTable();
                    }else {
                        showAlertDialog();
                    }
                }
            });
            return rootView;
        }else {
           showAlertDialog();

          return null;
        }
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error");
        builder.setMessage("Make sure you are connected to the Internet");

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void selectMeal(final String tableName) {
        db = dbHelper.getReadableDatabase();

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
        int totalPrice = 0;
        final String[] prices = new String[100];
        final String[] categories = new String[100];
        final String[] quantities = new String[100];
        while (cursor.moveToNext()){
            mealArrayAdapter.add(cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_NAME)));
            prices[i] = cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE));
            categories[i] = cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_CATEGORY));
            quantities[i] = cursor.getString(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_QUANTITY));
            totalPrice += (cursor.getInt(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE))*cursor.getInt(cursor.getColumnIndexOrThrow(OrderedMealsDbContract.MealEntry.COL_MEAL_QUANTITY)));
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

        final int finalTotalPrice = totalPrice;
        builderSingle.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(isNetworkAvailable()){
                    showProgressDialog();
                    uploadSelectedMeals(mealArrayAdapter,tableName,prices,categories,quantities, finalTotalPrice);
                }else {
                    showAlertDialog();
                }

            }
        });
        builderSingle.show();
        cursor.close();
    }

    private void uploadSelectedMeals(ArrayAdapter<String> arrayAdapter, String tableName, String[] prices, String[] categories, String[] quantities, int finalTotalPrice) {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        String timeNow = sdf.format(currentTime);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd:mm:yy");
        String dateToday = dateFormat.format(currentTime);
        String paymentState = "Not Paid";
        String serviceState = "Not Served";

        mDatabase = FirebaseDatabase.getInstance();
        myDBRef = mDatabase.getReference().child("orderedMeals").push();

        DatabaseReference table = myDBRef.child("tableName");
        DatabaseReference time = myDBRef.child("timeOrdered");
        DatabaseReference date = myDBRef.child("dateOrdered");
        DatabaseReference paymentStatus = myDBRef.child("paymentStatus");
        DatabaseReference serviceStatus = myDBRef.child("serviceStatus");
        DatabaseReference totalPrice = myDBRef.child("totalPrice");

        for(int i=0; i<arrayAdapter.getCount();i++){
            DatabaseReference mealsOrdered = myDBRef.child("MealsOrdered").push();

            DatabaseReference mealName = mealsOrdered.child("Meal");
            DatabaseReference mealCategory = mealsOrdered.child("Category");
            DatabaseReference mealPrice = mealsOrdered.child("Price");
            DatabaseReference mealQuantity = mealsOrdered.child("Quantity");

            mealName.setValue(arrayAdapter.getItem(i).toString());
            mealPrice.setValue(prices[i]);
            mealCategory.setValue(categories[i]);
            mealQuantity.setValue(quantities[i]);

        }
        table.setValue(tableName);
        time.setValue(timeNow);
        date.setValue(dateToday);
        paymentStatus.setValue(paymentState);
        serviceStatus.setValue(serviceState);
        totalPrice.setValue(String.valueOf(finalTotalPrice), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(getContext(),"You have successfully submitted your order",Toast.LENGTH_SHORT).show();
                db.execSQL("delete from " + OrderedMealsDbContract.MealEntry.TABLE_NAME);
                hideProgressDialog();
            }
        });

        category = "Food";
        //recyclerView.setLayoutManager(layoutManager);
        //Step 3: create an adapter
        MealAdapter adapter = new MealAdapter(mealsModelFoods,category);
        //Step 4: set the adapater
        recyclerView.setAdapter(adapter);
        //Step 5: Set item animator to defaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getContext(),"Saving Data","Please Wait",true);
    }

    private void hideProgressDialog() {
        progressDialog.dismiss();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
