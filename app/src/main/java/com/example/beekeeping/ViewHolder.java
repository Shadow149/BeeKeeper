package com.example.beekeeping;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {
    // Simple class to hold the name and location of the apiary within the view holder itself

    public TextView name;
    public TextView location;

    ViewHolder(View itemView){
        super(itemView);
        // Get name TextView from resources, which will hold the name in the ViewHolder
        name = itemView.findViewById(R.id.view_holder_apiary_name);
        // Get location TextView from resources, which will hold the location in the ViewHolder
        location = itemView.findViewById(R.id.view_holder_apiary_location);
    }
}
