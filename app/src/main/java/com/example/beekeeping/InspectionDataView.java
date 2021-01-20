package com.example.beekeeping;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import org.w3c.dom.Text;

import java.util.List;

public class InspectionDataView extends AppCompatActivity {

    private long hiveID;

    private TextView date;

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

    private RadioGroup weather;

    private TextView notes;
    private List<InspectionData> inspectionData;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_inspection_data);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            hiveID = extras.getLong("hiveID");
            position = extras.getInt("inspDataPos");
        }
        Database database = new Database(this);
        inspectionData = database.getInspectionData(hiveID);
        InspectionData data = inspectionData.get(position);

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

        Button addSuper = findViewById(R.id.addSuper);
        Button subSuper = findViewById(R.id.subSuper);
        TextView superAddAmount = findViewById(R.id.superAddAmount);

        weather = findViewById(R.id.weatherRadioGroup);
        notes = findViewById(R.id.notes);
        date = findViewById(R.id.dateTextBox);

        date.setText(data.getDate());
        queenBeePresent.setChecked(data.getQueenCellsPresent());
        queenCellsPresent.setChecked(data.getQueenBeePresent());
        broodEggs.setChecked(data.getBroodEggs());
        broodLarvae.setChecked(data.getBroodLarvae());
        broodCapped.setChecked(data.getBroodCapped());
        System.out.println(data.getNumFramesBrood());

        int framesNum = data.getNumFramesBrood();
        if (framesNum == -1){
            framesNum = 0;
        }

        numFramesBrood.setText(Integer.toString(framesNum));

        int storesNum = data.getStores();
        if (storesNum == -1){
            storesNum = 0;
        }
        stores.setText(Integer.toString(storesNum));

        room.setChecked(data.getRoom());
        varroaSeen.setChecked(data.getVarroaSeen());
        varroaTreatment.setChecked(data.getVarroaTreatment());

        LinearLayout layout = findViewById(R.id.add_inspection_data_layout);

        RadioButton beeTemperRadio = layout.findViewWithTag(data.getBeeTemper());
        if (beeTemperRadio != null){
            beeTemperRadio.setChecked(true);
        }

        feedGiven.setChecked(data.getFeedGiven());

        superAddAmount.setText(Integer.toString(data.getSupersAdded()));

        RadioButton weatherRadioButton = layout.findViewWithTag(data.getWeather());
        if (weatherRadioButton != null){
            weatherRadioButton.setChecked(true);
        }

        notes.setText(data.getNotes());


        queenBeePresent.setEnabled(false);
        queenCellsPresent.setEnabled(false);
        broodEggs.setEnabled(false);
        broodLarvae.setEnabled(false);
        broodCapped.setEnabled(false);
        numFramesBrood.setEnabled(false);
        stores.setEnabled(false);
        room.setEnabled(false);
        varroaSeen.setEnabled(false);
        varroaTreatment.setEnabled(false);

        for (int i = 0; i < beeTemper.getChildCount(); i ++){
            beeTemper.getChildAt(i).setEnabled(false);
        }

        feedGiven.setEnabled(false);

        addSuper.setEnabled(false);
        subSuper.setEnabled(false);

        for (int i = 0; i < weather.getChildCount(); i ++){
            weather.getChildAt(i).setEnabled(false);
        }

        notes.setEnabled(false);
        date.setEnabled(false);


    }
}