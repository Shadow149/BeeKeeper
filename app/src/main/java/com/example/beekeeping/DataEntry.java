package com.example.beekeeping;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataEntry {
    // Initialise Attributes
    private long ID;
    private String name;
    private String location;
    private int health;

    // Constructors, one with and without the ID in case needed
    DataEntry(String name, String location){
        this.name = name;
        this.location = location;
        this.health = -1;
    }
    DataEntry(long ID, String name, String location){
        this.ID = ID;
        this.name = name;
        this.location = location;
    }
    DataEntry(String name, String location, int health){
        this.name = name;
        this.location = location;
        this.health = health;
    }
    DataEntry(long ID, String name, String location, int health){
        this.ID = ID;
        this.name = name;
        this.location = location;
        this.health = health;
    }

    // Getters and setters

    public long getID() {
        return ID;
    }

    // Might remove for security reasons
    public void setID(long ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public static class Hive extends DataEntry {

        private DataEntry.Apiary parentApiary;
        private int totalFrames;
        private int totalSupers;
        private String reasons;

        Hive(String name, String location, int frames, int supers){
            super(name, location);
            this.totalFrames = frames;
            this.totalSupers = supers;
        }

        Hive(String name, String location, int frames, int supers, int health, String reasons){
            super(name, location, health);
            this.totalFrames = frames;
            this.totalSupers = supers;
            this.reasons = reasons;
        }
        Hive(long ID, String name, String location, DataEntry.Apiary parentApiary, int frames, int supers, int health, String reasons){
            super(ID, name, location, health);
            this.parentApiary = parentApiary;
            this.totalFrames = frames;
            this.totalSupers = supers;
            this.reasons = reasons;
        }

        public Apiary getParentApiary() {
            return parentApiary;
        }

        public void setParentApiary(Apiary parentApiary) {
            this.parentApiary = parentApiary;
        }

        public int getTotalFrames() {
            return totalFrames;
        }

        public void setTotalFrames(int totalFrames) {
            this.totalFrames = totalFrames;
        }

        public int getTotalSupers() {
            return totalSupers;
        }

        public void setTotalSupers(int totalSupers) {
            this.totalSupers = totalSupers;
        }

        public String getReasons() {
            return reasons;
        }

        public void setReasons(String reasons) {
            this.reasons = reasons;
        }
    }

    public static class Apiary extends DataEntry {
        Apiary(String name, String location){
            super(name, location);
        }
        Apiary(long ID, String name, String location){
            super(ID, name, location);
        }
    }

    public static class Health{
        private int health;
        private Set<String> reasons = new HashSet<>();

        Health(int health){
            this.health = health;
        }

        Health(int health, Set<String> reasons){
            this.health = health;
            this.reasons = reasons;
        }

        public Set<String> getReasons() {
            return reasons;
        }

        public String getSerialisedReasons(){
            //https://stackoverflow.com/questions/5703330/saving-arraylists-in-sqlite-databases/26277173
            JSONObject json = new JSONObject();
            try {
                json.put("reasons", new JSONArray(reasons));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String arrayList = json.toString();
            return arrayList;
        }

        public void setHealth(int health) {
            if (health < this.health){
                this.health = health;
            }
        }
        public void removeReason(String reason){
            reasons.remove(reason);
        }

        public void addReason(String reason) {
            reasons.add(reason);
        }

        public int getHealth() {
            return health;
        }

        public void setReasons(Set<String> reasons) {
            this.reasons = reasons;
        }

        public String getHealthString(){
            if (health == 0) {
                return "Bad";
            }
            else if (health == 1){
                return "Medium";
            }
            else{
                return "Good";
            }
        }
        public String getHealthColour(){
            if (health == 0) {
                return "#bf2424";
            }
            else if (health == 1){
                return "#d6911a";
            }
            else{
                return "#59D534";
            }
        }
    }

}
