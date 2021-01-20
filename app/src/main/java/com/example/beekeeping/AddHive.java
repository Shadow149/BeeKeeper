package com.example.beekeeping;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class AddHive extends AppCompatActivity {

    private TextView hiveName; // TextView object from activity layout - used to change hive name in the activity
    private TextView hiveLocation; // TextView object from activity layout - used to change hive location in the activity

    private int frameCount = 0;
    private int superCount = 0;

    private long apiaryID; // Store parent apiary ID

    /*
	=========================================================
	  onCreate - Method called when the activity is
	  initialised.
	=========================================================
	*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hive); // Set content of activity via the layout XML.

        hiveName = findViewById(R.id.nameText); // Get hive name text view from layout
        hiveLocation = findViewById(R.id.locationText); // Get hive location text view from layout

        Bundle extras = getIntent().getExtras(); // Get extra data passed into the activity via an intent
        if (extras != null) {
            apiaryID = extras.getLong("apiaryId"); // Get parent apiary pass from the intent
        }
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
        inflater.inflate(R.menu.add_toolbar, menu); // Set content of activity via the layout XML add_toolbar.xml.
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
            // Create a new hive entry using the data within the fields,
            // and then go back to the main activity.
            Database database = new Database(this);

            // Create a new hive entry
            DataEntry.Hive hive = new DataEntry.Hive(
                    hiveName.getText().toString(),
                    hiveLocation.getText().toString(),
                    frameCount,
                    superCount
            );

            long id = database.addHive(apiaryID, hive); // Add hive to database
            database.print_record_by_id(id, "hivestable"); // Test that the entry was successful
        }
        onBackPressed(); // Use android's back button press functionality to go back
        return true;
    }

    /*
	=========================================================
	  updateSuperText - Method for updating the super count
	  text within the activity layout to the private variable
	  superCount
	=========================================================
	*/
    private void updateSuperText(){
        TextView superAddAmount = findViewById(R.id.superAddAmount); // Get add super amount text view from activity layout
        superAddAmount.setText(Integer.toString(superCount));
    }

    /*
	=========================================================
	  updateFrameText - Method for updating the frame count
	  text within the activity layout to the private variable
	  frameCount
	=========================================================
	*/
    private void updateFrameText(){
        TextView superAddAmount = findViewById(R.id.addFramesAmount); // Get add frames amount text view from activity layout
        superAddAmount.setText(Integer.toString(frameCount));
    }

    public void increaseFrameAmount(View view){
        frameCount ++;
        updateFrameText();
    }

    public void decreaseFrameAmount(View view){
        if (frameCount > 0){
            frameCount --;
        }
        updateFrameText();
    }

    public void increaseSuperAmount(View view){
        superCount ++;
        updateSuperText();
    }

    public void decreaseSuperAmount(View view){
        if (superCount > 0){
            superCount --;
        }
        updateSuperText();
    }


}