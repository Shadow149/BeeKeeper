package com.example.beekeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

public class HiveView extends AppCompatActivity {

    private long apiaryID;
    private String apiaryName;
    List<DataEntry.Hive> hives;
    Database database;
    HiveAdapter adapter;
    TextView apiaryHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hive_view);
        TextView title = findViewById(R.id.hiveApiaryName);
        apiaryHealth = findViewById(R.id.apiaryHealthText);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            apiaryName = extras.getString("apiaryName");
            title.setText(apiaryName);

            apiaryID = extras.getLong("apiaryId");
        }

        // Initialise database
        database = new Database(this);

        // Get the apiaries from the table
        hives = database.getHives(apiaryID);

        updateApiaryHealth();

        // Get the recycler view resource
        RecyclerView recyclerView = findViewById(R.id.hive_list);
        // Set the layout manager for it
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get the custom adapter used for handling the view holders and items(HiveAdapter.java)
        adapter = new HiveAdapter(this, hives);
        // Set the adapter
        recyclerView.setAdapter(adapter);

    }

    // Method to add the toolbar to the activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Get the resource main_activity_toolbar.xml and add to the activity menu
        inflater.inflate(R.menu.main_activity_toolbar, menu);
        return true;
    }

    // Method that is called when an option button (button on menu/tool bar) is pressed
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // There is only one button, so an if statement is not needed. We know if a button is pressed, its the add button.
        // Open the AddApiary activity
        Intent operation = new Intent(this, AddHive.class);
        operation.putExtra("apiaryId", apiaryID);
//        operation.putExtra("apiaryName", apiaryName);
        startActivity(operation);
        //finish();
        return true;
    }

    private void updateApiaryHealth(){
        DataEntry.Health health = calculateApiaryHealth(hives);
        if (health != null) {
            apiaryHealth.setText(health.getHealthString());
            apiaryHealth.setTextColor(Color.parseColor(health.getHealthColour()));
        }
    }

    private DataEntry.Health calculateApiaryHealth(List<DataEntry.Hive> hives){
        int totalHealth = 0;
        for(DataEntry.Hive hive : hives){
            long id = hive.getID();
            try {
                System.out.println(hive.getHealth());
                DataEntry.Health hiveHealth = HiveInspectionView.calculateHealth(this, id, hive.getHealth(), hive.getReasons(), hive.getTotalFrames());
                if (hiveHealth != null){
                    totalHealth += hiveHealth.getHealth();
                } else {
                    totalHealth += 2;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (hives.size() > 0){
            return new DataEntry.Health(Math.round(totalHealth/hives.size()));
        } else {
            return new DataEntry.Health(2);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        hives = database.getHives(apiaryID);
        adapter.setHives(hives);
        adapter.notifyItemInserted(adapter.getItemCount());

        updateApiaryHealth();
    }
}