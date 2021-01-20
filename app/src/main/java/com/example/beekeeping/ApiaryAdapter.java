package com.example.beekeeping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ApiaryAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final LayoutInflater inflater; // inflater used to add layout to a view holder
    private List<DataEntry.Apiary> apiaries; // List of apiary objects of the user's data
    private Context context;

    /*
	=========================================================
	  ApiaryAdapter - Constructor to declare private
	  variables.
	=========================================================
	*/
    ApiaryAdapter(Context context, List<DataEntry.Apiary> apiaries){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.apiaries = apiaries;
    }

    /*
	=========================================================
	  onCreateViewHolder - Method used to create a new view
	  holder (An item in the list - a card)
	=========================================================
	*/
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.apiary_view_holder, parent, false); // Add layout/template used for each item
        return new ViewHolder(view);
    }

    /*
	=========================================================
	  onBindViewHolder - Method used to populate the view
	  holder with information
	  Used to display the apiary name and location, and
	  setting an on click listener for when the user clicks
	  it.
	=========================================================
	*/
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataEntry.Apiary apiary = apiaries.get(position); // Get an apiary from the apiary list, with index = position of view holder in list

        final long id = apiary.getID(); // Get apiary id
        final String name = apiary.getName(); // Get apiary name
        String location = apiary.getLocation(); // Get apiary location

        holder.name.setText(name); // Set view holder name text as the apiary name
        holder.location.setText(location); // Set the view holder location text as the apiary location

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent operation = new Intent(context, HiveView.class); // Create new intent for a hive view activity
                // Pass data into the next activity
                operation.putExtra("apiaryName", name); // Add apiary name
                operation.putExtra("apiaryId", id); // Add apiary ID
                context.startActivity(operation); // Start activity (show hive activity)

                //System.out.println("Pressed");
                //((Activity) context).finish();
            }
        });

    }

    /*
   =========================================================
     getItemCount - Get the number of elements in the
     recycler view list
   =========================================================
   */
    @Override
    public int getItemCount() {
        return apiaries.size();
    }

    public void setApiaries(List<DataEntry.Apiary> apiaries) {
        this.apiaries = apiaries;
    }
}
