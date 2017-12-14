package com.dannextech.apps.hotelsystem.FinalSystem;


import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dannextech.apps.hotelsystem.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookViewSpecificMeals extends Fragment {

    DatabaseReference mReference,mReference2,mReference3;

    TextView tvMealsOrdered;
    RecyclerView rvViewOrderedMeals;
    Button btServeMeals;
    ProgressDialog progressDialog;

    FirebaseRecyclerAdapter<ViewOrderedMealsModel,WaiterViewOrderedMealsHolder> firebaseRecyclerAdapter;


    public CookViewSpecificMeals() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (isNetworkAvailable()){
            showProgressDialog();
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_waiter_view_specific_meals, container, false);

            String url = getArguments().getString("ref");

            Toast.makeText(getContext(),"Network is "+ url,Toast.LENGTH_SHORT).show();
            System.out.println(url);

            mReference = FirebaseDatabase.getInstance().getReference(url.substring(37));//71
            mReference2 = FirebaseDatabase.getInstance().getReference(url.substring(37,71)).child("tableName");
            mReference3 = FirebaseDatabase.getInstance().getReference(url.substring(37,71)).child("serviceStatus");

            rvViewOrderedMeals = (RecyclerView) view.findViewById(R.id.rvViewOrderedMealsWaiter);
            tvMealsOrdered = (TextView) view.findViewById(R.id.tvTableNameTitle);
            btServeMeals = (Button) view.findViewById(R.id.btServeMeals);

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
                    mReference3.setValue("SERVED", new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            Fragment fragment = new CookViewUnservedMeals();
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.flCookFragment,fragment);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    });

                }
            });

            mReference.keepSynced(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);

            //rvViewOrderedMeals.setHasFixedSize(true);
            rvViewOrderedMeals.setLayoutManager(layoutManager);

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<ViewOrderedMealsModel,WaiterViewOrderedMealsHolder>(
                    ViewOrderedMealsModel.class,R.layout.view_ordered_meals_details,WaiterViewOrderedMealsHolder.class,mReference
            ) {
                @Override
                protected void populateViewHolder(WaiterViewOrderedMealsHolder viewHolder, ViewOrderedMealsModel model, int position) {
                    viewHolder.setMealName(model.getMeal());
                    viewHolder.setQuantity(model.getQuantity());

                    hideProgressDialog();
                }
            };

            rvViewOrderedMeals.setAdapter(firebaseRecyclerAdapter);

            return view;
        }else {
            showAlertDialog();
            return null;
        }
    }

    public static class WaiterViewOrderedMealsHolder extends RecyclerView.ViewHolder{
        View itemView;
        public WaiterViewOrderedMealsHolder(View itemView) {
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

    private void showProgressDialog() {
        progressDialog = ProgressDialog.show(getContext(),"Retriving Data","Please Wait",true);
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
    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Error");
        builder.setMessage("Make sure you are connected to the Internet");

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
