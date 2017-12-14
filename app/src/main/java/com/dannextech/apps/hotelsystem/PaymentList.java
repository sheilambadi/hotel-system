package com.dannextech.apps.hotelsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.dannextech.apps.hotelsystem.FinalSystem.OrderedMealListModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentList extends AppCompatActivity {
    RecyclerView rvPaymentList;

    DatabaseReference mReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_list);
        rvPaymentList = (RecyclerView) findViewById(R.id.rvPaymentList);

        mReference = FirebaseDatabase.getInstance().getReference().child("orderedMeals");

        mReference.keepSynced(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        rvPaymentList.setHasFixedSize(true);
        rvPaymentList.setLayoutManager(layoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OrderedMealListModel,PaymentListViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderedMealListModel, PaymentListViewHolder>(OrderedMealListModel.class,R.layout.payment_details,PaymentListViewHolder.class,mReference) {
            @Override
            protected void populateViewHolder(PaymentListViewHolder viewHolder, OrderedMealListModel model, int position) {
                viewHolder.setTableName(model.getTableName());
                viewHolder.setTimeOrdered(model.getTimeOrdered());
                viewHolder.setPaidStatus(model.getPaymentStatus());
                viewHolder.setTotalPrice(model.getTotalPrice());
            }
        };

        rvPaymentList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PaymentListViewHolder extends RecyclerView.ViewHolder{
        View mview;

        public PaymentListViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
        }

        public void setTableName(String tableName){
            TextView tvTableName = (TextView) mview.findViewById(R.id.tvTableName);
            tvTableName.setText(tableName);
        }

        public void setPaidStatus(String paidStatus){
            TextView tvPaidStatus = (TextView) mview.findViewById(R.id.tvStatus);
            tvPaidStatus.setText(paidStatus);
        }
        public void setTimeOrdered(String timeOrdered){
            TextView tvTimeOrdered = (TextView) mview.findViewById(R.id.tvTimeOrdered);
            tvTimeOrdered.setText(timeOrdered);
        }
        public void setTotalPrice(String totalPrice){
            TextView tvTotalPrice = (TextView) mview.findViewById(R.id.tvTotalPrice);
            tvTotalPrice.setText(totalPrice);
        }

    }
}
