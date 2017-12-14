package com.dannextech.apps.hotelsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dannextech.apps.hotelsystem.FinalSystem.ViewOrderedMealsModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewOrderedMeals extends AppCompatActivity {

    DatabaseReference mReference,mReference2,mReference3;

    TextView tvMealsOrdered;
    RecyclerView rvViewOrderedMeals;
    Button btServeMeals;

    FirebaseRecyclerAdapter<ViewOrderedMealsModel,ViewOrderedMealsHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_ordered_meals);

        final Intent intent = getIntent();
        String url = intent.getStringExtra("ref");

        System.out.println(url);

        mReference = FirebaseDatabase.getInstance().getReference(url.substring(37));//71
        mReference2 = FirebaseDatabase.getInstance().getReference(url.substring(37,71)).child("tableName");
        mReference3 = FirebaseDatabase.getInstance().getReference(url.substring(37,71)).child("serviceStatus");

        rvViewOrderedMeals = (RecyclerView) findViewById(R.id.rvViewOrderedMeals);
        tvMealsOrdered = (TextView) findViewById(R.id.tvTableNameTitle);
        btServeMeals = (Button) findViewById(R.id.btServeMeals);

        mReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tvMealsOrdered.setText(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btServeMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mReference3.setValue("SERVED");
                Intent intent1 = new Intent(ViewOrderedMeals.this,OrderedMealList.class);
                startActivity(intent1);
            }
        });

        mReference.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvViewOrderedMeals.setHasFixedSize(true);
        rvViewOrderedMeals.setLayoutManager(layoutManager);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ViewOrderedMealsModel, ViewOrderedMealsHolder>(
                ViewOrderedMealsModel.class,R.layout.view_ordered_meals_details,ViewOrderedMealsHolder.class,mReference
        ) {
            @Override
            protected void populateViewHolder(ViewOrderedMealsHolder viewHolder, ViewOrderedMealsModel model, int position) {
                viewHolder.setMealName(model.getMeal());
                viewHolder.setQuantity(model.getQuantity());
            }
        };

        rvViewOrderedMeals.setAdapter(firebaseRecyclerAdapter);
    }

    public static class ViewOrderedMealsHolder extends RecyclerView.ViewHolder{
        View itemView;
        public ViewOrderedMealsHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void setMealName(String mealName){
            TextView tvMealName = (TextView) itemView.findViewById(R.id.tvMealName);
            tvMealName.setText(mealName);
        }
        public void setQuantity(String quantity){
            TextView tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            tvQuantity.setText(quantity);
        }
    }


}
