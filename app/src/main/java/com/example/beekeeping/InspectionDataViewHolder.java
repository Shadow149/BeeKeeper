package com.example.beekeeping;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class InspectionDataViewHolder extends RecyclerView.ViewHolder {
    // Simple class to hold the name and location of the apiary within the view holder itself

    public TextView date;

    InspectionDataViewHolder(View itemView){
        super(itemView);
        // Get date TextView from resources, which will hold the date in the ViewHolder
        date = itemView.findViewById(R.id.insp_view_holder_date);
    }
}