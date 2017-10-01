package com.dannextech.apps.hotelsystem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class PaymentList extends AppCompatActivity {
    RecyclerView rvPaymentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_list);
        rvPaymentList = (RecyclerView) findViewById(R.id.rvPaymentList);

        rvPaymentList.setLayoutManager(new LinearLayoutManager(this));
    }
}
