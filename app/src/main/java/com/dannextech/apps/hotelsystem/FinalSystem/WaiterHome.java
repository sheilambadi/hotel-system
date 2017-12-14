package com.dannextech.apps.hotelsystem.FinalSystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dannextech.apps.hotelsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterHome extends Fragment {

    ImageView ivMealImage,ivDrinkImage,ivSnackImage;
    CardView cvMealImage, cvDrinkImage,cvSnackImage;
    TextView tvWelcomeWaiter;
    public WaiterHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_waiter_home, container, false);

        ivMealImage = view.findViewById(R.id.ivMealsImage);
        ivDrinkImage = view.findViewById(R.id.ivDrinksImage);
        ivSnackImage = view.findViewById(R.id.ivSnackImage);
        cvDrinkImage = view.findViewById(R.id.cvDrinksImage);
        cvMealImage = view.findViewById(R.id.cvMealImage);
        cvSnackImage = view.findViewById(R.id.cvSnacksImage);
        tvWelcomeWaiter = view.findViewById(R.id.tvWelcomeWaiter);

        Glide.with(getContext()).load(R.drawable.download).override(150,120).into(ivMealImage);
        Glide.with(getContext()).load(R.drawable.download2).override(150,120).into(ivDrinkImage);
        Glide.with(getContext()).load(R.drawable.download3).override(150,120).into(ivSnackImage);

        cvMealImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WaiterMakeOrder();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.flWaiterFragment,fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        cvDrinkImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WaiterMakeOrder();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.flWaiterFragment,fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        cvSnackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new WaiterMakeOrder();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.flWaiterFragment,fragment);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        return view;
    }

}
