package com.example.beekeeping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HiveInspectionView extends AppCompatActivity {
	private static int GOOD = 2;
	private static int MID = 1;
	private static int BAD = 0;

	private static String VARROA_SEEN = "Varroa Mite Seen!";
	private static String NO_ROOM = "No room in hive";
	private static String QUEEN_CELLS_SEEN = "Queen cells seen";
	private static String TOO_LITTLE_FEED = "Not enough feed";
	private static String OVERCROWDING = "Overcrowding";
	private static String AGGRESSIVE = "Bees are too aggressive";


	private String hiveName;
	private long hiveID;
	private int hiveFrames;
	private int hiveSupers;
	private String apiaryName;
	private long apiaryID;
	private int previousHiveHealth;
	private String previousHiveReasons;

	Database database;
	List<InspectionData> inspectionData;
	RecyclerView recyclerView;
	InspectionDataAdapter inspectionDataAdapter;

	TextView reasonsText;
	TextView hiveHealthText;
	TextView superAmount;

	AlarmManager notificationAlarmManager;
	PendingIntent pendingIntent;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hive_inspection_view);

		TextView title = findViewById(R.id.inspectionHiveName);
		TextView frameAmount = findViewById(R.id.inspectionHiveFrames);
		superAmount = findViewById(R.id.inspectionHiveSupers);
		hiveHealthText = findViewById(R.id.hiveHealthText);
		reasonsText = findViewById(R.id.hiveReasonsText);
		//https://stackoverflow.com/questions/1748977/making-textview-scrollable-on-android
		reasonsText.setMovementMethod(new ScrollingMovementMethod());

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			hiveName = extras.getString("hiveName");
			hiveFrames = extras.getInt("hiveFrames");
			hiveSupers = extras.getInt("hiveSupers");
			previousHiveHealth = extras.getInt("hiveHealth");
			previousHiveReasons = extras.getString("hiveReasons");
			title.setText(hiveName);

			frameAmount.setText("Frames: " + hiveFrames);
			superAmount.setText("Supers: " + hiveSupers);

			hiveID = extras.getLong("hiveId");
			apiaryName = extras.getString("apiaryName");
			apiaryID = extras.getLong("apiaryId");
		}

		database = new Database(this);
		// Get the apiaries from the table
		inspectionData = database.getInspectionData(hiveID);
		// Get the recycler view resource
		recyclerView = findViewById(R.id.insp_data_list);
		// Set the layout manager for it
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		// Get the custom adapter used for handling the view holders and items(InspectionDataAdapter.java)
		inspectionDataAdapter = new InspectionDataAdapter(this, inspectionData, hiveID);
		// Set the adapter
		recyclerView.setAdapter(inspectionDataAdapter);

		try {
			updateHealth();
		} catch (JSONException e) {
			e.printStackTrace();
		}


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
		// Open the AddInspectionData activity
		Intent operation = new Intent(this, AddInspectionData.class);
		operation.putExtra("hiveId", hiveID);
		operation.putExtra("hiveSupers", hiveSupers);
		startActivity(operation);
		return true;
	}

	public static DataEntry.Health calculateHealth(Context context, long id, int previousHiveHealth, String previousHiveReasons, int hiveFrames) throws JSONException {
		Database database = new Database(context);
		List<InspectionData> data = database.getInspectionData(id);

		DataEntry.Hive.Health previousHealth;
		DataEntry.Hive.Health health;
		InspectionData latestEntry;

		if (data.size() > 0) {
			if (data.size() > 1){
				previousHealth = new DataEntry.Health(previousHiveHealth);
				previousHealth.setReasons(HiveInspectionView.deserialiseReasons(previousHiveReasons));
			}else{
				previousHealth = new DataEntry.Health(GOOD);
			}

			health = new DataEntry.Health(GOOD);
			health.setReasons(new HashSet<>(previousHealth.getReasons()));

			latestEntry = data.get(data.size() - 1);
		} else {
			return null;
		}

		for (String reason : previousHealth.getReasons()) {
			if (reason.equals(VARROA_SEEN)) {
				if (latestEntry.getVarroaTreatment()) {
					health.setHealth(MID);
				}
				if (!latestEntry.getVarroaSeen()) {
					health.setHealth(GOOD);
					health.removeReason(VARROA_SEEN);
				}
			}
			if (!latestEntry.getQueenCellsPresent()) {
				if (reason.equals(QUEEN_CELLS_SEEN)) {
					health.setHealth(GOOD);
					health.removeReason(QUEEN_CELLS_SEEN);
				}
			}
			if (reason.equals(TOO_LITTLE_FEED)) {
				if (latestEntry.getFeedGiven()) {
					health.setHealth(GOOD);
					health.removeReason(TOO_LITTLE_FEED);
				}
			}
			if (reason.equals(OVERCROWDING)) {
				if (latestEntry.getRoom() && latestEntry.getSupersAdded() > 0) {
					health.setHealth(GOOD);
					health.removeReason(OVERCROWDING);
				}
			}
			if (reason.equals(NO_ROOM)) {
				if (latestEntry.getRoom() || latestEntry.getSupersAdded() > 0) {
					health.setHealth(GOOD);
					health.removeReason(NO_ROOM);
				}
			}
			if (reason.equals(AGGRESSIVE)) {
				if (!latestEntry.getBeeTemper().equals("Aggressive")){
					health.setHealth(GOOD);
					health.removeReason(AGGRESSIVE);
				}
			}
		}

		float feedRatio;
		if (hiveFrames != 0){
			feedRatio = (float)latestEntry.getStores() / (float)hiveFrames;
		} else {
			feedRatio = 0;
		}


		if (feedRatio > 0.7){
			health.setHealth(MID);
			health.addReason(OVERCROWDING);
		} else if (feedRatio < 0.3) {
			if (!latestEntry.getFeedGiven()){
				health.setHealth(MID);
				health.addReason(TOO_LITTLE_FEED);
				health.addReason(OVERCROWDING);
			}
		}

		if (latestEntry.getVarroaSeen()){
			health.setHealth(BAD);
			health.addReason(VARROA_SEEN);
		}

		if (latestEntry.getBeeTemper().equals("Aggressive")){
			health.setHealth(MID);
			health.addReason(AGGRESSIVE);
		}

		if (latestEntry.getQueenCellsPresent()) {
			health.setHealth(BAD);
			health.addReason(QUEEN_CELLS_SEEN);
		}

		if (!latestEntry.getRoom()) {
			health.setHealth(MID);
			health.addReason(NO_ROOM);
		}

		return health;

	}

	private void scheduleNotification(String title, String content){
		System.out.println("yyy");
		Intent notifyIntent = new Intent(this, NotificationReceiver.class);
		notifyIntent.putExtra("title", title);
		notifyIntent.putExtra("content", content);
		pendingIntent = PendingIntent.getBroadcast(this, NotificationIntentService.createID(), notifyIntent, PendingIntent.FLAG_CANCEL_CURRENT);

		Calendar notificationTime = Calendar.getInstance();
		notificationTime.setTimeInMillis(System.currentTimeMillis());
		notificationTime.set(Calendar.HOUR_OF_DAY, 10);

		notificationAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		notificationAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP,  notificationTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
	}



	private String reasonsSetToString(Set<String> reasons){
		String reasonsTextString = "";
		for (String reason: reasons){
			reasonsTextString += reason + "\n";
		}
		return reasonsTextString;
	}

	private void updateHealth() throws JSONException {
		DataEntry.Health hiveHealth = calculateHealth(this, hiveID, previousHiveHealth, previousHiveReasons, hiveFrames);
        System.out.println("KILL ME ------------------------------------------------------");
		System.out.println("hiveHealth");
		System.out.println(hiveHealth);
		//create notification
		if (hiveHealth != null) {
			if (hiveHealth.getReasons().size() > 0) {
				scheduleNotification(apiaryName + ", " + hiveName + "'s bees need you!", reasonsSetToString(hiveHealth.getReasons()));
			} else {
				if (notificationAlarmManager != null) {
					notificationAlarmManager.cancel(pendingIntent);
				}
			}
		}


		if (hiveHealth != null){
		    System.out.println("HIVE HEALTH DATA");
		    System.out.println(hiveHealth.getHealth());
            System.out.println(hiveHealth.getReasons());
            System.out.println(hiveHealth.getSerialisedReasons());
			updateHiveHealth(hiveHealth);
			hiveHealthText.setText(hiveHealth.getHealthString());
			hiveHealthText.setTextColor(Color.parseColor(hiveHealth.getHealthColour()));

			String reasonsTextString = reasonsSetToString(hiveHealth.getReasons());

			reasonsText.setText(reasonsTextString);
		}
	}

	private void updateHiveHealth(DataEntry.Health hiveHealth) {
		database.updateHiveHealth(hiveHealth, hiveID);
	}

	private static Set<String> deserialiseReasons(String reasons) throws JSONException {
	    System.out.println("reasons");
	    System.out.println(reasons);
		JSONObject json = new JSONObject(reasons);
		//https://www.baeldung.com/convert-list-to-set-and-set-to-list
		JSONArray reasonsJSON = json.optJSONArray("reasons");
		ArrayList<String> reasonsList = new ArrayList<String>();
		if (reasonsJSON != null) {
			for (int i=0; i<reasonsJSON.length(); i++){
				reasonsList.add(reasonsJSON.get(i).toString());
			}
		}
		Set<String> reasonsSet = new HashSet<>(reasonsList);
		System.out.println("REASON SET" +  reasonsSet);
		return reasonsSet;
	}

	@Override
	protected void onResume() {
		super.onResume();
		inspectionData = database.getInspectionData(hiveID);
		inspectionDataAdapter.setInspectionData(inspectionData);
		inspectionDataAdapter.notifyItemInserted(inspectionDataAdapter.getItemCount());

		DataEntry.Hive newHiveData = database.getHive(hiveID);
		if (newHiveData != null){
			hiveSupers = newHiveData.getTotalSupers();
			previousHiveHealth = newHiveData.getHealth();
			previousHiveReasons = newHiveData.getReasons();
			superAmount.setText("Supers: " + hiveSupers);
		}

		try {
			updateHealth();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}