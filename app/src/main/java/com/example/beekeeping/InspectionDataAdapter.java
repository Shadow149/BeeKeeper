package com.example.beekeeping;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InspectionDataAdapter extends RecyclerView.Adapter<InspectionDataViewHolder> {

	private final LayoutInflater inflater;
	private List<InspectionData> inspectionData;
	private Context context;
	private long hiveID;

	InspectionDataAdapter(Context context, List<InspectionData> inspectionData, long hiveID){
		this.context = context;
		this.inflater = LayoutInflater.from(context);
		this.inspectionData = inspectionData;
		this.hiveID = hiveID;
	}

	// Method used to create a new view holder (item in the list)
	@NonNull
	@Override
	public InspectionDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		// Add layout/template used for each item
		View view = inflater.inflate(R.layout.inspection_data_view_holder, parent, false);
		return new InspectionDataViewHolder(view);
	}

	// Method used when displaying the data to a view holder
	@Override
	public void onBindViewHolder(@NonNull InspectionDataViewHolder holder, final int position) {
		InspectionData data = inspectionData.get(position);

		String date = data.getDate();

		holder.date.setText(date);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent operation = new Intent(context, InspectionDataView.class);
				operation.putExtra("hiveID", hiveID);
				operation.putExtra("inspDataPos", position);
				context.startActivity(operation);
			}
		});

	}

	// Method used to get the number of elements used in the list (recycler view)
	@Override
	public int getItemCount() {
		return inspectionData.size();
	}

	public void setInspectionData(List<InspectionData> inspectionData) {
		this.inspectionData = inspectionData;
	}

}
