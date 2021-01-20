package com.example.beekeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

public class ApiaryView extends AppCompatActivity {

    // Initialise Attributes
    Database database;
    RecyclerView recyclerView;
    ApiaryAdapter apiaryAdapter;
    List<DataEntry.Apiary> apiaries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apiary_view);

        // Initialise database
        database = new Database(this);

        // Get the apiaries from the table
        apiaries = database.getApiaries();
        // Get the recycler view resource
        recyclerView = findViewById(R.id.apiary_list);
        // Set the layout manager for it
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Get the custom adapter used for handling the view holders and items(ApiaryAdapter.java)
        apiaryAdapter = new ApiaryAdapter(this, apiaries);
        // Set the adapter
        recyclerView.setAdapter(apiaryAdapter);
        //long id = database.addHive(1,"Hive 1", "Left");
        //database.print_record_by_id(id, "hivestable");

        //database.firstRecordLinkingTable();
    }

    // Method to test records are added correctly
    public void onClickAddApiary(View view){
        // Test data for expected data
        DataEntry.Apiary apiary = new DataEntry.Apiary("Apiary 1", "Field 1");
        // Added apiary to database
        long id = database.addApiary(apiary);
        //database.print_record_by_id(id);
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
        Intent operation = new Intent(this, AddApiary.class);
        startActivity(operation);
        //finish();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        apiaries = database.getApiaries();
        apiaryAdapter.setApiaries(apiaries);
        apiaryAdapter.notifyItemInserted(apiaryAdapter.getItemCount());
    }
}