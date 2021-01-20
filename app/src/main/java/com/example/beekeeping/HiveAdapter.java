package com.example.beekeeping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HiveAdapter extends RecyclerView.Adapter<ViewHolder> {

    private final LayoutInflater inflater;
    private List<DataEntry.Hive> hives;
    private Context context;

    HiveAdapter(Context context, List<DataEntry.Hive> hives){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.hives = hives;
    }

    // Method used to create a new view holder (item in the list)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Add layout/template used for each item
        View view = inflater.inflate(R.layout.hive_view_holder, parent, false);
        return new ViewHolder(view);
    }

    // Method used when displaying the data to a view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataEntry.Hive hive = hives.get(position);

        final long id = hive.getID();
        final String name = hive.getName();
        String location = hive.getLocation();
        final int frames = hive.getTotalFrames();
        final int supers = hive.getTotalSupers();
        final int health = hive.getHealth();
        final String reasons = hive.getReasons();

        DataEntry.Apiary apiary = hive.getParentApiary();
        final String apiaryName = apiary.getName();
        final long apiaryID = apiary.getID();

        holder.name.setText(name);
        holder.location.setText(location);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Pressed");
                Intent operation = new Intent(context, HiveInspectionView.class);
                operation.putExtra("hiveName", name);
                operation.putExtra("hiveFrames", frames);
                operation.putExtra("hiveSupers", supers);
                operation.putExtra("hiveHealth", health);
                operation.putExtra("hiveReasons", reasons);
                operation.putExtra("hiveId", id);
                operation.putExtra("apiaryName", apiaryName);
                operation.putExtra("apiaryId", apiaryID);
                context.startActivity(operation);
            }
        });

    }

    // Method used to get the number of elements used in the list (recycler view)
    @Override
    public int getItemCount() {
        return hives.size();
    }

    public void setHives(List<DataEntry.Hive> hives){
        this.hives = hives;
    }

}
