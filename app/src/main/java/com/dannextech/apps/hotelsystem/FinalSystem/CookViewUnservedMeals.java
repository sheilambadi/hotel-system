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
import android.widget.TextView;
import android.widget.Toast;

import com.dannextech.apps.hotelsystem.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookViewUnservedMeals extends Fragment {

    RecyclerView rvOrderedMealList;
    ProgressDialog progressDialog;

    public DatabaseReference mReference,itemRef;

    List<Listdetails> myList;
    FirebaseRecyclerAdapter<OrderedMealListModel,WaiterViewOrderedViewHolder> firebaseRecyclerAdapter;


    public CookViewUnservedMeals() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (isNetworkAvailable()){
            Toast.makeText(getContext(),"Network is "+ isNetworkAvailable(),Toast.LENGTH_SHORT).show();
            showProgressDialog();
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_waiter_view_ordered_meals, container, false);

            rvOrderedMealList = (RecyclerView) view.findViewById(R.id.rvOrderedMealList);

            mReference = FirebaseDatabase.getInstance().getReference().child("orderedMeals");

            mReference.keepSynced(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setReverseLayout(true);
            layoutManager.setStackFromEnd(true);

            rvOrderedMealList.setHasFixedSize(true);
            rvOrderedMealList.setLayoutManager(layoutManager);

            Query dbQuery = mReference.orderByChild("serviceStatus").equalTo("Not Served");
            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderedMealListModel, WaiterViewOrderedViewHolder>(
                    OrderedMealListModel.class,R.layout.ordered_meal_details,WaiterViewOrderedViewHolder.class,dbQuery
            ) {
                @Override
                protected void populateViewHolder(WaiterViewOrderedViewHolder viewHolder, OrderedMealListModel model, final int position) {
                    viewHolder.setTableName(model.getTableName());
                    viewHolder.setServedStatus(model.getServiceStatus());
                    viewHolder.setTimeOrdered(model.getTimeOrdered());
                    viewHolder.setTotalPrice("sh " +model.getTotalPrice());

                    hideProgressDialog();
                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Fragment fragment = new CookViewSpecificMeals();
                            Bundle args = new Bundle();
                            args.putString("ref",firebaseRecyclerAdapter.getRef(position).child("MealsOrdered").toString());
                            fragment.setArguments(args);
                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                            fragmentTransaction.replace(R.id.flCookFragment,fragment);
                            fragmentTransaction.commitAllowingStateLoss();

                        }
                    });


                }

            };
            rvOrderedMealList.setAdapter(firebaseRecyclerAdapter);

            return view;
        }else {
            showAlertDialog();
            return null;
        }
    }

    public static class WaiterViewOrderedViewHolder extends RecyclerView.ViewHolder{
        View mview;
        public WaiterViewOrderedViewHolder(View itemView) {
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
