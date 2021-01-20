package com.example.beekeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.DatePicker;

import com.google.android.material.chip.Chip;

public class AddInspectionData extends AppCompatActivity {
    private long hiveID; // Parent hive database id

    private TextView date; // Date text view from the activity layout
    private DatePickerDialog datePicker; // Date picker dialog used to show the date picker

    // Inspection data field variables from activity layout
    private CheckBox queenCellsPresent;
    private CheckBox queenBeePresent;
    private Chip broodEggs;
    private Chip broodLarvae;
    private Chip broodCapped;
    private TextView numFramesBrood;
    private TextView stores;
    private CheckBox room;
    private CheckBox varroaSeen;
    private CheckBox varroaTreatment;
    private RadioGroup beeTemper;
    private CheckBox feedGiven;
    private int supersAdded = 0;
    private int hiveSupers;
    private RadioGroup weather;
    private TextView notes;


    /*
	=========================================================
	  onCreate - Method called when the activity is
	  initialised.
	=========================================================
	*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inspection_data); // Set content of activity via the layout XML activity_add_inspection_data.xml.

        Bundle extras = getIntent().getExtras(); // Get extra data passed into the activity via an intent
        if (extras != null) {
            hiveID = extras.getLong("hiveId"); // Get parent hive id
            hiveSupers = extras.getInt("hiveSupers"); // Get parent hive super amount
        }

        // Declare all of the inspection data fields from the activity layout
        queenBeePresent = findViewById(R.id.queenBeeCheck);
        queenCellsPresent = findViewById(R.id.queenCellsCheck);
        broodEggs = findViewById(R.id.eggsChip);
        broodLarvae = findViewById(R.id.larvaeChip);
        broodCapped = findViewById(R.id.cappedChip);
        numFramesBrood = findViewById(R.id.framesOfBrood);
        stores = findViewById(R.id.stores);
        room = findViewById(R.id.roomCheck);
        varroaSeen = findViewById(R.id.varroaSeen);
        varroaTreatment = findViewById(R.id.varroaTreatmentGiven);
        beeTemper = findViewById(R.id.temperRadioGroup);
        feedGiven = findViewById(R.id.feedGivenCheck);
        weather = findViewById(R.id.weatherRadioGroup);
        notes = findViewById(R.id.notes);

        date = findViewById(R.id.dateTextBox);
        date.setInputType(InputType.TYPE_NULL);


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
            // Create a new inspection data entry using the data within the fields,
            // and then go back to the main activity.
            Database database = new Database(this);

            InspectionData inspectionData = new InspectionData(); // Create a new inspection data entry

            // =============================================
            //   Set the various inspection data properties
            // =============================================
            inspectionData.setDate(date.getText().toString());

            inspectionData.setQueenBeePresent(queenBeePresent.isChecked());
            inspectionData.setQueenCellsPresent(queenCellsPresent.isChecked());

            inspectionData.setBroodEggs(broodEggs.isChecked());
            inspectionData.setBroodLarvae(broodLarvae.isChecked());
            inspectionData.setBroodCapped(broodCapped.isChecked());

            int framesOfBroodInt = -1; // Set default value to -1 if not selected
            String framesOfBroodString = numFramesBrood.getText().toString();
            // If the frames of brood integer field is filled in
            if (!framesOfBroodString.equals("")){
                framesOfBroodInt = Integer.valueOf(framesOfBroodString);
            }
            inspectionData.setNumFramesBrood(framesOfBroodInt);

            int storesInt = -1; // Set default value to -1 if not selected
            String storesString = stores.getText().toString();
            // If the stores integer field is selected
            if (!storesString.equals("")){
                storesInt = Integer.valueOf(storesString);
            }
            inspectionData.setStores(storesInt);

            inspectionData.setRoom(room.isChecked());
            inspectionData.setVarroaSeen(varroaSeen.isChecked());
            inspectionData.setVarroaTreatment(varroaTreatment.isChecked());

            // Get the checked button id from the radio button *GROUP*
            RadioButton checkedTemper = findViewById(beeTemper.getCheckedRadioButtonId());
            // If there actually is a checked button
            if (checkedTemper != null){
                inspectionData.setBeeTemper(checkedTemper.getText().toString());
            } else {
                inspectionData.setBeeTemper("None");
            }

            inspectionData.setFeedGiven(feedGiven.isChecked());
            inspectionData.setSupersAdded(supersAdded);

            // Get the checked button id from the radio button *GROUP*
            RadioButton weatherRadio = findViewById(weather.getCheckedRadioButtonId());
            // If there actually is a checked button
            if (weatherRadio != null){
                inspectionData.setWeather(weatherRadio.getText().toString());
            } else {
                inspectionData.setWeather("None");
            }

            inspectionData.setNotes(notes.getText().toString());

            long id = database.addInspectionData(hiveID, inspectionData); // Add inspection data to database
            // Update super amount of hive if supers have been added
            if (supersAdded > 0){
                database.updateHiveSupers(hiveSupers + supersAdded, hiveID);
            }
            database.print_record_by_id_insp(id, "datatable"); // Test that the entry was successful
        }
        onBackPressed(); // Use android's back button press functionality to go back
        return true;
    }

    /*
	=========================================================
	  updateSuperText - Method for updating the super count
	  text within the activity layout to the private variable
	  supersAdded
	=========================================================
	*/
    private void updateSuperText(){
        TextView superAddAmount = findViewById(R.id.superAddAmount); // Get add super amount text view from activity layout
        superAddAmount.setText(Integer.toString(supersAdded));
    }

    /*
	=========================================================
	  onDateClick - Method called when the date text view
	  field is clicked.
	  Used to display the date picker dialog so the user can
	  pick a date.
	=========================================================
	*/
    public void onDateClick(View v) {
        Calendar calendar = Calendar.getInstance(); // Create a new calendar object
        int day = calendar.get(Calendar.DAY_OF_MONTH); // Get current day
        int month = calendar.get(Calendar.MONTH); // Get current month
        int year = calendar.get(Calendar.YEAR); // Get current year

        // Create a new DatePickerDialog so the user can chose a date. Specifying
        // the OnDateSetListener and the year, month and day for the dialog
        // to start on
        datePicker = new DatePickerDialog(AddInspectionData.this,
                new DatePickerDialog.OnDateSetListener() {
                    /*
                    =========================================================
                      onDateSet - Method used as a OnDateSetListener - called
                      when the user has chosen a date
                    =========================================================
                    */
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year); // set date property as the user's chosen date
                    }
                }, year, month, day);

        datePicker.show(); // Show date picker dialog
    }

    public void decreaseSuperAmount(View view){
        if (supersAdded > 0){
            supersAdded --;
        }
        updateSuperText();
    }

    public void increaseSuperAmount(View view){
        supersAdded ++;
        updateSuperText();
    }
}