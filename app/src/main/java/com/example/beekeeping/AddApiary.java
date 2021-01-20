package com.example.beekeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class AddApiary extends AppCompatActivity {

	private TextView apiaryName; // TextView object from activity layout - used to change apiary name in the activity
	private TextView apiaryLocation; // TextView object from activity layout - used to change apiary location in the activity

	/*
	=========================================================
	  onCreate - Method called when the activity is
	  initialised.
	=========================================================
	*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_apiary); // Set content of activity via the layout XML activity_add_apiary.xml.

		this.apiaryName = findViewById(R.id.nameText); // Get apiary name text view from layout
		this.apiaryLocation = findViewById(R.id.locationText); // Get apiary location text view from layout
	}

	/*
	=========================================================
	  onCreateOptionsMenu - Method to add the toolbar to the
	  activity.
	=========================================================
	*/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.add_toolbar, menu); // Get the resource add_toolbar.xml and add to the activity menu
		return true;
	}

	/*
	=========================================================
	  onOptionsItemSelected - Method called when toolbar
	  options item is pressed.
	  MenuItem parameter specifies which button is pressed.
	=========================================================
	*/
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		// There are multiple buttons on the menu, so an if statement is needed
		// If the button pressed was the save button
		if (item.getItemId() == R.id.save_button){
			// Create a new apiary entry using the data within the fields,
			// and then go back to the main activity.
			Database database = new Database(this);

			// Create a new apiary entry
			DataEntry.Apiary apiary = new DataEntry.Apiary(
					apiaryName.getText().toString(),
					apiaryLocation.getText().toString()
			);

			long id = database.addApiary(apiary); // Add apiary to database
			database.print_record_by_id(id, "apiariestable"); // Test that the entry was successful
		}
		onBackPressed(); // Use android's back button press functionality to go back
		return true;
	}

}