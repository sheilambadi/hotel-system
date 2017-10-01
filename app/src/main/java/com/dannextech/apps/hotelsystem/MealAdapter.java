package com.dannextech.apps.hotelsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

/**
 * Created by amoh on 9/25/2017.
 */

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.ViewHolder> {
    MealsModel[] mealsModel;
    Context context;
    ColorGenerator generator = ColorGenerator.MATERIAL;

    String category;

    OrderedMealsDbHelper dbHelper;

    int[] selected = new int[100];


    public MealAdapter(MealsModel[] mealsModel,String category) {
        this.mealsModel = mealsModel;
        //this.context = context;
        this.category = category;
    }

    //create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.meal_details,parent,false);
        //Set the view's size, margins, padding and layout parameters
        ViewHolder vh = new ViewHolder(v);
        context = parent.getContext();
        dbHelper = new OrderedMealsDbHelper(context);
        return vh;
    }

    //Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //Get element from your datset at this position
        //replace the contents of the viw with that element
        String name = mealsModel[position].getName();
        String price = mealsModel[position].getPrice();
        String quantity = mealsModel[position].getQuantity();
        holder.mealTitle.setText(name);
        holder.mealPrice.setText(price);
        holder.mealQuatity.setText(quantity);

        //Get the first letter of list item
        String letter = String.valueOf(name.charAt(0));
        //Create a new TextDrawable for our image's background
        final TextDrawable drawable = TextDrawable.builder().buildRound(letter,generator.getRandomColor());
        holder.letter.setImageDrawable(drawable);
        //Toast.makeText(context,"Size is "+ position,Toast.LENGTH_LONG).show();
        final int[] pos = {-1};
        holder.mealTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pos[0] = selected[position];
                if (pos[0] ==-1||pos[0]==100000){
                    holder.letter.setImageDrawable(drawable);
                    selected[position] = 100000;
                    deleteUnSelectedDb(holder.mealTitle.getText().toString());
                }
                else {
                    holder.letter.setImageResource(R.drawable.ok_filled);
                    selected[position] = position;
                    saveSelectedDb(holder.mealTitle.getText().toString(),holder.mealPrice.getText().toString(),holder.mealQuatity.getText().toString());
                }
            }
        });
    }

    private void deleteUnSelectedDb(String s) {
        //gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Defne 'where' part of query
        String selection = OrderedMealsDbContract.MealEntry.COL_MEAL_NAME + " LIKE ?";
        //Specify argument in plac holder
        String[] selectioAgs = {s};
        //Isue SQL statement
        db.delete(OrderedMealsDbContract.MealEntry.TABLE_NAME,selection,selectioAgs);
    }

    private void saveSelectedDb(String mealTitle, String mealPrice,String mealQuantity) {
        //gets the data repository in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //Creates a new map of values, where columns are the keys
        ContentValues values = new ContentValues();
        values.put(OrderedMealsDbContract.MealEntry.COL_MEAL_NAME, mealTitle);
        values.put(OrderedMealsDbContract.MealEntry.COL_MEAL_PRICE, mealPrice);
        values.put(OrderedMealsDbContract.MealEntry.COL_MEAL_QUANTITY,mealQuantity);
        values.put(OrderedMealsDbContract.MealEntry.COL_MEAL_CATEGORY,category);
        Toast.makeText(context,mealTitle +","+mealPrice+","+category,Toast.LENGTH_SHORT).show();
        //insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(OrderedMealsDbContract.MealEntry.TABLE_NAME,null,values);
    }

    @Override
    public int getItemCount() {
        return mealsModel.length==0 ? 0: mealsModel.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mealTitle, mealPrice;
        EditText mealQuatity;
        ImageView letter;

        public ViewHolder(View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.ivMealLetter);
            mealPrice = itemView.findViewById(R.id.tvMealPrice);
            mealTitle = itemView.findViewById(R.id.tvMealTitle);
            mealQuatity = itemView.findViewById(R.id.etQuantity);
        }
    }
}
