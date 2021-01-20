package com.example.beekeeping;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    //####### Attributes #######

    // Initialise database name and version info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BeeKeepingDatabase";

    // Initialise Apiary table column titles
    private static final String APIARY_TABLE_NAME = "apiariestable";
    private static final String APIARY_ID = "id";
    private static final String APIARY_NAME = "name";
    private static final String APIARY_LOCATION = "location";
    // Add image column

    // Initialise Hive table column titles
    private static final String HIVE_TABLE_NAME = "hivestable";
    private static final String HIVE_ID = "id";
    private static final String HIVE_NAME = "name";
    private static final String HIVE_LOCATION = "location";
    private static final String HIVE_FRAMES = "frames";
    private static final String HIVE_SUPERS = "supers";
    private static final String HIVE_HEALTH = "health";
    private static final String HIVE_REASONS = "reasons";
    // Add image column
    // Add customisation for colour?

    // Initialise Hive-Apiary Joining table field titles
    private static final String HIVE_APIARY_LINK_TABLE = "hiveapiarylinktable";
    private static final String HIVE_APIARY_LINK_APIARY_ID = "apiaryid";
    private static final String HIVE_APIARY_LINK_HIVE_ID = "hiveid";

    // Initialise Inspection data table field titles
    private static final String INSP_TABLE_NAME = "datatable";
    private static final String INSP_ID = "id";
    private static final String INSP_DATE = "date";
    private static final String INSP_QUEEN_CELLS_PRESENT = "queenCellsPresent";
    private static final String INSP_QUEEN_BEE_PRESENT = "queenBeePresent";
    private static final String INSP_BROOD_EGGS = "broodEggs";
    private static final String INSP_BROOD_LARVAE = "broodLarvae";
    private static final String INSP_BROOD_CAPPED = "broodCapped";
    private static final String INSP_FRAMES_BROOD = "numFramesBrood";
    private static final String INSP_STORES = "stores";
    private static final String INSP_ROOM = "room";
    private static final String INSP_VARROA_SEEN = "varroaSeen";
    private static final String INSP_VARROA_TREATMENT = "varroaTreatment";
    private static final String INSP_TEMPER = "beeTemper";
    private static final String INSP_FEED_GIVEN = "feedGiven";
    private static final String INSP_SUPERS_ADDED = "supersAdded";
    private static final String INSP_WEATHER = "weather";
    private static final String INSP_NOTES = "notes";

    // Initialise Hive-Inspection Data Joining table field titles
    private static final String HIVE_INSP_JOIN_TABLE = "hiveinsplinktable";
    private static final String HIVE_INSP_JOIN_INSP_ID = "inspid";
    private static final String HIVE_INSP_JOIN_HIVE_ID = "hiveid";


    Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create Apiary table in the database
        String apiaryTableCreationQuery = "CREATE TABLE " + APIARY_TABLE_NAME + " (" + APIARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1 ," + APIARY_NAME + " TEXT,"+ APIARY_LOCATION + " TEXT)";
        sqLiteDatabase.execSQL(apiaryTableCreationQuery);

        String hiveTableCreationQuery = "CREATE TABLE " + HIVE_TABLE_NAME + " (" + HIVE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1 ," + HIVE_NAME + " TEXT,"+ HIVE_LOCATION + " TEXT,"+ HIVE_FRAMES + " INTEGER,"+ HIVE_SUPERS + " INTEGER," + HIVE_HEALTH + " INTEGER," + HIVE_REASONS + " STRING)";
        sqLiteDatabase.execSQL(hiveTableCreationQuery);

        String apiaryHiveLinkTableCreationQuery = "CREATE TABLE " + HIVE_APIARY_LINK_TABLE + " (" + HIVE_APIARY_LINK_APIARY_ID + " INTEGER," + HIVE_APIARY_LINK_HIVE_ID + " INTEGER)";
        sqLiteDatabase.execSQL(apiaryHiveLinkTableCreationQuery);

        String inspectionTableCreationQuery = "CREATE TABLE " + INSP_TABLE_NAME + " (" +
                INSP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1 ," +
                INSP_DATE + " STRING," +
                INSP_QUEEN_CELLS_PRESENT + " INTEGER," +
                INSP_QUEEN_BEE_PRESENT + " INTEGER," +
                INSP_BROOD_EGGS + " INTEGER," +
                INSP_BROOD_LARVAE + " INTEGER," +
                INSP_BROOD_CAPPED + " INTEGER," +
                INSP_FRAMES_BROOD + " INTEGER," +
                INSP_STORES + " INTEGER," +
                INSP_ROOM + " INTEGER," +
                INSP_VARROA_SEEN + " INTEGER," +
                INSP_VARROA_TREATMENT + " INTEGER," +
                INSP_TEMPER + " STRING," +
                INSP_FEED_GIVEN + " INTEGER," +
                INSP_SUPERS_ADDED + " INTEGER," +
                INSP_WEATHER + " STRING," +
                INSP_NOTES + " STRING)";

        sqLiteDatabase.execSQL(inspectionTableCreationQuery);

        String hiveInspJoinTableCreationQuery = "CREATE TABLE " + HIVE_INSP_JOIN_TABLE + " (" + HIVE_INSP_JOIN_HIVE_ID + " INTEGER," + HIVE_INSP_JOIN_INSP_ID + " INTEGER)";
        sqLiteDatabase.execSQL(hiveInspJoinTableCreationQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // If I decide to update the DATABASE_VERSION var, this function will be called to delete the table to make sure there isn't version issues.
        // Don't do anything if version isn't updated
        if(oldVersion >= newVersion)
            return;
        // Delete table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + APIARY_TABLE_NAME);
    }

    // Method to add an apiary record into the table
    public long addApiary(DataEntry.Apiary apiary){
        // Open the database for writing
        SQLiteDatabase database = this.getWritableDatabase();
        // Create a ContentValues class for the data to go into the database
        ContentValues content = new ContentValues();

        // Put the apiary data into the content class
        content.put(APIARY_NAME, apiary.getName());
        content.put(APIARY_LOCATION, apiary.getLocation());

        // Insert the data into the table, returning the id of the new record
        long ID = database.insert(APIARY_TABLE_NAME, null, content);

        // If an error occurred when inserting the data
        if (ID == -1){
            System.out.println("An error occurred when inserting a new record");
        } else {
            System.out.println("Entry successful");
        }
        database.close();
        return ID;
    }

    // Method to add a hive record into the hive table
    public long addHive(long apiaryID, DataEntry.Hive hive){
        // Open the database for writing
        SQLiteDatabase database = this.getWritableDatabase();
        // Create a ContentValues class for the data to go into the database
        ContentValues hive_content = new ContentValues();

        // Put the apiary data into the content class
        hive_content.put(HIVE_NAME, hive.getName());
        hive_content.put(HIVE_LOCATION, hive.getLocation());
        hive_content.put(HIVE_FRAMES, hive.getTotalFrames());
        hive_content.put(HIVE_SUPERS, hive.getTotalSupers());
        hive_content.put(HIVE_HEALTH, hive.getHealth());
        hive_content.put(HIVE_REASONS, hive.getReasons());

        // Insert the data into the table, returning the id of the new record
        long ID = database.insert(HIVE_TABLE_NAME, null, hive_content);

        //Create a ContentValues class for the data to go into the linking table.
        ContentValues linking_content = new ContentValues();

        //Put the data into the content class
        linking_content.put(HIVE_APIARY_LINK_APIARY_ID,apiaryID);
        linking_content.put(HIVE_APIARY_LINK_HIVE_ID,ID);

        //Inter data into table, no id is needed as there is no primary field in the linking table.
        database.insert(HIVE_APIARY_LINK_TABLE, null, linking_content);

        // If an error occurred when inserting the data
        if (ID == -1){
            System.out.println("An error occurred when inserting a new record into hive table");
        } else {
            System.out.println("Entry successful into hive table");
        }
        database.close();
        return ID;
    }

    public void updateHiveHealth(DataEntry.Health health, long hiveID){
        // Open the database for writing
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(HIVE_HEALTH, health.getHealth());
        content.put(HIVE_REASONS, health.getSerialisedReasons());
        // Query the database table
        database.update(HIVE_TABLE_NAME, content, HIVE_ID+"="+hiveID, null);
        database.close();
    }

    public void updateHiveSupers(int newSupers, long hiveID){
        // Open the database for writing
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues content = new ContentValues();
        content.put(HIVE_SUPERS, newSupers);
        // Query the database table
        database.update(HIVE_TABLE_NAME, content, HIVE_ID+"="+hiveID, null);
        database.close();
    }

    // Method to add a hive record into the hive table
    public long addInspectionData(long hiveID, InspectionData inspectionData){
        // Open the database for writing
        SQLiteDatabase database = this.getWritableDatabase();
        // Create a ContentValues class for the data to go into the database
        ContentValues data_content = new ContentValues();

        // Initialise Inspection data table field titles
        System.out.println(inspectionData.getDate());
        data_content.put(INSP_DATE, inspectionData.getDate());
        data_content.put(INSP_QUEEN_CELLS_PRESENT, inspectionData.getQueenCellsPresent());
        data_content.put(INSP_QUEEN_BEE_PRESENT, inspectionData.getQueenBeePresent());
        data_content.put(INSP_BROOD_EGGS, inspectionData.getBroodEggs());
        data_content.put(INSP_BROOD_LARVAE, inspectionData.getBroodLarvae());
        data_content.put(INSP_BROOD_CAPPED, inspectionData.getBroodCapped());
        data_content.put(INSP_FRAMES_BROOD, inspectionData.getNumFramesBrood());
        data_content.put(INSP_STORES, inspectionData.getStores());
        data_content.put(INSP_ROOM, inspectionData.getRoom());
        data_content.put(INSP_VARROA_SEEN, inspectionData.getVarroaSeen());
        data_content.put(INSP_VARROA_TREATMENT, inspectionData.getVarroaTreatment());
        data_content.put(INSP_TEMPER, inspectionData.getBeeTemper());
        data_content.put(INSP_FEED_GIVEN, inspectionData.getFeedGiven());
        data_content.put(INSP_SUPERS_ADDED, inspectionData.getSupersAdded());
        data_content.put(INSP_WEATHER, inspectionData.getWeather());
        data_content.put(INSP_NOTES, inspectionData.getNotes());

        // Insert the data into the table, returning the id of the new record
        long ID = database.insert(INSP_TABLE_NAME, null, data_content);

        //Create a ContentValues class for the data to go into the linking table.
        ContentValues linking_content = new ContentValues();

        //Put the data into the content class
        linking_content.put(HIVE_INSP_JOIN_HIVE_ID, hiveID);
        linking_content.put(HIVE_INSP_JOIN_INSP_ID, ID);

        //Inter data into table, no id is needed as there is no primary field in the linking table.
        long joinID = database.insert(HIVE_INSP_JOIN_TABLE, null, linking_content);

        // If an error occurred when inserting the data
        if (ID == -1){
            System.out.println("An error occurred when inserting a new record into inspection table");
        } else {
            System.out.println("Entry successful into hive table");
        }

        // If an error occurred when inserting the data
        if (joinID == -1){
            System.out.println("An error occurred when inserting a new record into inspection-hive joining table");
        } else {
            System.out.println("Entry successful into hive table");
        }
        database.close();
        return ID;
    }

    public DataEntry.Hive getHive(long hiveID) {
        SQLiteDatabase read_database = this.getReadableDatabase();
        DataEntry.Hive hive = null;

        // select * from hivetable WHERE hiveID = hiveID
        String query = "SELECT * FROM "+HIVE_TABLE_NAME+" WHERE "+HIVE_ID+"="+hiveID;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        // If the result from the query is successful and the moveToFirst() method is called successfully
        // e.g. no errors. MoveToFirst also move the cursor to the first result, will produce -1
        // if there are no results.
        if(result != null && result.moveToFirst()) {
            //there should only be one result
            String hiveName = result.getString(1);
            String hiveLocation = result.getString(2);
            int hiveFrames = result.getInt(3);
            int hiveSupers = result.getInt(4);
            int hiveHealth = result.getInt(5);
            String hiveReasons = result.getString(6);
            hive = new DataEntry.Hive(hiveName,hiveLocation,hiveFrames,hiveSupers,hiveHealth,hiveReasons);
        }
        result.close();
        return hive;
    }


    public List<DataEntry.Apiary> getApiaries(){
        // Test for what the first entry in the database is, to check the record was added correctly
        SQLiteDatabase read_database = this.getReadableDatabase();

        // Create list for all apiary entries
        List<DataEntry.Apiary> apiaries = new ArrayList<>();

        // select * from apiariestable
        String query = "SELECT * FROM "+APIARY_TABLE_NAME;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        // If the result from the query is successful and the moveToFirst() method is called successfully
        // e.g. no errors. MoveToFirst also move the cursor to the first result, will produce -1
        // if there are no results.
        if(result != null && result.moveToFirst()) {
            do {
                // Gets a single record
                // Create a new apiary class with the data from the record
                DataEntry.Apiary a = new DataEntry.Apiary(result.getLong(0), result.getString(1), result.getString(2));
                // Add to list
                apiaries.add(a);
            }
            // While a moveToNext method call is successful e.g. there is a record next
            while (result.moveToNext());
        } else {
            // There is either no data in the table or an error occurred so the query returned nothing
            System.out.println("No data");
        }
        // Close cursor
        result.close();
        read_database.close();
        // Return list
        return apiaries;
    }

    public List<DataEntry.Hive> getHives(long apiaryID){
        // Test for what the first entry in the database is, to check the record was added correctly
        SQLiteDatabase read_database = this.getReadableDatabase();

        // Create list for all apiary entries
        List<DataEntry.Hive> hives = new ArrayList<>();

        // e.g. select hiveid from hiveapiarylinkstable where apiaryid = 0
        String query = "SELECT "+HIVE_APIARY_LINK_HIVE_ID+" FROM "+HIVE_APIARY_LINK_TABLE+" WHERE " + HIVE_APIARY_LINK_APIARY_ID + " = " +apiaryID;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        // If the result from the query is successful and the moveToFirst() method is called successfully
        // e.g. no errors. MoveToFirst also move the cursor to the first result, will produce -1
        // if there are no results.
        if(result != null && result.moveToFirst()) {
            do {
                // Gets a single record
                // Add to list
                long hiveID = result.getLong(0);

                // e.g. select * from hivestable where hiveid = id
                String hiveQuery = "SELECT * FROM "+HIVE_TABLE_NAME+" WHERE " + HIVE_ID + " = " +hiveID;
                System.out.println(hiveQuery);
                // Query the database table
                Cursor hiveQueryResult = read_database.rawQuery(hiveQuery, null);

                String apiaryQuery = "SELECT * FROM "+APIARY_TABLE_NAME+" WHERE " + APIARY_ID + " = " + apiaryID;
                System.out.println(apiaryQuery);
                // Query the database table
                Cursor apiaryQueryResult = read_database.rawQuery(apiaryQuery, null);

                if(hiveQueryResult != null && hiveQueryResult.moveToFirst()) {
                    // Gets a single record

                    String hiveName = hiveQueryResult.getString(1);
                    String hiveLocation = hiveQueryResult.getString(2);
                    int hiveFrames = hiveQueryResult.getInt(3);
                    int hiveSupers = hiveQueryResult.getInt(4);
                    int hiveHealth = hiveQueryResult.getInt(5);
                    String hiveReasons = hiveQueryResult.getString(6);

                    if (apiaryQueryResult != null && apiaryQueryResult.moveToFirst()) {
                        // Create a new apiary class with the data from the record
                        DataEntry.Apiary apiary = new DataEntry.Apiary(apiaryID, apiaryQueryResult.getString(1), apiaryQueryResult.getString(2));
                        // Create a new hive class with the data from the record
                        DataEntry.Hive hive = new DataEntry.Hive(hiveID, hiveName, hiveLocation, apiary, hiveFrames, hiveSupers, hiveHealth, hiveReasons);
                        // Add to list
                        hives.add(hive);
                    }
                }
                hiveQueryResult.close();
                apiaryQueryResult.close();

            }
            // While a moveToNext method call is successful e.g. there is a record next
            while (result.moveToNext());
        } else {
            // There is either no data in the table or an error occurred so the query returned nothing
            System.out.println("No data");
        }
        // Close cursor
        result.close();
        read_database.close();
        // Return list
        return hives;
    }

    public List<InspectionData> getInspectionData(long hiveID) {
        // Test for what the first entry in the database is, to check the record was added correctly
        SQLiteDatabase read_database = this.getReadableDatabase();

        // Create list for all apiary entries
         List<InspectionData> inspectionData = new ArrayList<>();

        // e.g. select inspid from hiveinsplinkstable where hiveid = hiveid
        System.out.println(hiveID);
        String query = "SELECT "+HIVE_INSP_JOIN_INSP_ID+" FROM "+HIVE_INSP_JOIN_TABLE+" WHERE " + HIVE_INSP_JOIN_HIVE_ID + " = " +hiveID;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        // If the result from the query is successful and the moveToFirst() method is called successfully
        // e.g. no errors. MoveToFirst also move the cursor to the first result, will produce -1
        // if there are no results.
        if(result != null && result.moveToFirst()) {
            do {
                // Gets a single record
                // Add to list
                long inspID = result.getLong(0);

                // e.g. select * from insptable where inspid = id
                String inspQuery = "SELECT * FROM "+INSP_TABLE_NAME+" WHERE " + INSP_ID + " = " +inspID;
                System.out.println(inspQuery);
                // Query the database table
                Cursor inspQueryResult = read_database.rawQuery(inspQuery, null);

                if(inspQueryResult != null && inspQueryResult.moveToFirst()) {
                    // Gets a single record
                        InspectionData inspData = new InspectionData();

                        inspData.setID(inspQueryResult.getInt(0));
                        inspData.setDate(inspQueryResult.getString(1));
                        inspData.setQueenCellsPresent(inspQueryResult.getInt(2) == 1);
                        inspData.setQueenBeePresent(inspQueryResult.getInt(3) == 1);
                        inspData.setBroodEggs(inspQueryResult.getInt(4) == 1);
                        inspData.setBroodLarvae(inspQueryResult.getInt(5) == 1);
                        inspData.setBroodCapped(inspQueryResult.getInt(6) == 1);
                        inspData.setNumFramesBrood(inspQueryResult.getInt(7));
                        inspData.setStores(inspQueryResult.getInt(8));
                        inspData.setRoom(inspQueryResult.getInt(9) == 1);
                        inspData.setVarroaSeen(inspQueryResult.getInt(10) == 1);
                        inspData.setVarroaTreatment(inspQueryResult.getInt(11) == 1);
                        inspData.setBeeTemper(inspQueryResult.getString(12));
                        inspData.setFeedGiven(inspQueryResult.getInt(13) == 1);
                        inspData.setSupersAdded(inspQueryResult.getInt(14));
                        inspData.setWeather(inspQueryResult.getString(15));
                        inspData.setNotes(inspQueryResult.getString(16));
                        // Add to list
                        inspectionData.add(inspData);

                }
                inspQueryResult.close();

            }
            // While a moveToNext method call is successful e.g. there is a record next
            while (result.moveToNext());
        } else {
            // There is either no data in the table or an error occurred so the query returned nothing
            System.out.println("No data");
        }
        // Close cursor
        result.close();
        read_database.close();
        // Return list
        return inspectionData;
    }

    public void print_record_by_id(long id, String table_name){
        // Test for what the first entry in the database is, to check the record was added correctly
        SQLiteDatabase read_database = this.getReadableDatabase();

        // select * from table_name where id = 1
        String query = "SELECT * FROM "+table_name+" WHERE id = "+ id;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        if(result != null && result.moveToFirst()) {
            // Print results
            System.out.println(result.getLong(0));
            System.out.println(result.getString(1));
            System.out.println(result.getString(2));

        } else {
            System.out.println("No data");
        }
        result.close();
        read_database.close();
    }

    public void print_record_by_id_insp(long id, String table_name){
        // Test for what the first entry in the database is, to check the record was added correctly
        SQLiteDatabase read_database = this.getReadableDatabase();

        // select * from table_name where id = 1
        String query = "SELECT * FROM "+table_name+" WHERE id = "+ id;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        if(result != null && result.moveToFirst()) {
            // Print results
            System.out.println(result.getLong(0));
            System.out.println(result.getString(1));
            System.out.println(result.getInt(2));

        } else {
            System.out.println("No data");
        }
        result.close();
        read_database.close();
    }

    public void firstRecordLinkingTable(){
        // Test for what the first entry in the database is, to check the record was added correctly
        SQLiteDatabase read_database = this.getReadableDatabase();

        // select * from table_name where id = 1
        String query = "SELECT * FROM "+HIVE_APIARY_LINK_TABLE;
        System.out.println(query);
        // Query the database table
        Cursor result = read_database.rawQuery(query, null);

        if(result != null && result.moveToFirst()) {
            // Print results
            System.out.println(result.getLong(0));
            System.out.println(result.getLong(1));
        } else {
            System.out.println("No data");
        }
        result.close();
        read_database.close();
    }


}
