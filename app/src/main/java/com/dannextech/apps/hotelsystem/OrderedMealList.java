package com.dannextech.apps.hotelsystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dannextech.apps.hotelsystem.FinalSystem.MealInfoModel;
import com.dannextech.apps.hotelsystem.FinalSystem.OrderedMealListModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class OrderedMealList extends AppCompatActivity {
    RecyclerView rvOrderedMealList;

    public DatabaseReference mReference,itemRef;

    List<MealInfoModel> myList;
    FirebaseRecyclerAdapter<OrderedMealListModel,OrderedMealListViewHolder> firebaseRecyclerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ordered_meal_list);

        rvOrderedMealList = (RecyclerView) findViewById(R.id.rvOrderedMealList);

        mReference = FirebaseDatabase.getInstance().getReference().child("orderedMeals");

        mReference.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvOrderedMealList.setHasFixedSize(true);
        rvOrderedMealList.setLayoutManager(layoutManager);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderedMealListModel, OrderedMealListViewHolder>(
                OrderedMealListModel.class,R.layout.ordered_meal_details,OrderedMealListViewHolder.class,mReference
        ) {
            @Override
            protected void populateViewHolder(OrderedMealListViewHolder viewHolder, OrderedMealListModel model, final int position) {
                viewHolder.setTableName(model.getTableName());
                viewHolder.setServedStatus(model.getServiceStatus());
                viewHolder.setTimeOrdered(model.getTimeOrdered());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        itemRef = firebaseRecyclerAdapter.getRef(position).child("Meals Ordered");
                        Intent intent = new Intent(OrderedMealList.this,ViewOrderedMeals.class);
                        intent.putExtra("ref",firebaseRecyclerAdapter.getRef(position).child("MealsOrdered").toString());
                        startActivity(intent);

                    }
                });

            }
        };
        rvOrderedMealList.setAdapter(firebaseRecyclerAdapter);

    }

    public void setItemClickedRef(DatabaseReference itemClickedRef) {
        itemRef = itemClickedRef;
    }
    public DatabaseReference getItemClickedRef(){
        return itemRef;
    }

    public static class OrderedMealListViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public OrderedMealListViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTableName(String tableName){
            TextView tvTableName = (TextView) mview.findViewById(R.id.tvTableName);
            tvTableName.setText(tableName);
        }

        public void setServedStatus(String servedStatus){
            TextView tvServedStatus = (TextView) mview.findViewById(R.id.tvStatus);
            tvServedStatus.setText(servedStatus);
        }
        public void setTimeOrdered(String timeOrdered){
            TextView tvTimeOrdered = (TextView) mview.findViewById(R.id.tvTimeOrdered);
            tvTimeOrdered.setText(timeOrdered);
        }
        public void setTotalPrice(String totalPrice){
            TextView tvTotalMealPrice = (TextView) mview.findViewById(R.id.tvTotalMealPrice);
            tvTotalMealPrice.setText(totalPrice);
        }


    }
}
