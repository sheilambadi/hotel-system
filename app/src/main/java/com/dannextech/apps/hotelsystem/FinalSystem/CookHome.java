package com.dannextech.apps.hotelsystem.FinalSystem;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dannextech.apps.hotelsystem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CookHome extends Fragment {

    TextView tvWelcomeCook;
    Button btServeMealsCook;

    public CookHome() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cook_home, container, false);

        tvWelcomeCook = (TextView) view.findViewById(R.id.tvWelcomeCook);
        btServeMealsCook = (Button) view.findViewById(R.id.btServeMealsCook);

        return view;
    }

}
