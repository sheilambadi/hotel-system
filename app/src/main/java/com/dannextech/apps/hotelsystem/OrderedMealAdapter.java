package com.dannextech.apps.hotelsystem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dannextech.apps.hotelsystem.FinalSystem.MealInfoModel;

import java.util.List;

/**
 * Created by amoh on 10/2/2017.
 */

public class OrderedMealAdapter extends RecyclerView.Adapter<OrderedMealAdapter.ViewHolder> {
    List<MealInfoModel> listdata;

    public OrderedMealAdapter(List<MealInfoModel> listdata) {
        this.listdata = listdata;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_meal_details,parent,false);
        ViewHolder myHolder = new ViewHolder(view);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MealInfoModel data = listdata.get(position);
        holder.tvTableName.setText(data.getTableName());
        holder.tvTimeOrdered.setText(data.getTimeOrdered());
        holder.tvServedStatus.setText(data.getServiceStatus());
    }

    @Override
    public int getItemCount() {
        if (listdata == null)
            return 0;
        else
            return listdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTableName,tvTimeOrdered,tvServedStatus;
        public ViewHolder(View view) {
            super(view);
            tvServedStatus = (TextView) view.findViewById(R.id.tvStatus);
            tvTableName = (TextView) view.findViewById(R.id.tvTableName);
            tvTimeOrdered = (TextView) view.findViewById(R.id.tvTimeOrdered);
        }
    }
}
