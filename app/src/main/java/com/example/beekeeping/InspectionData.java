package com.example.beekeeping;

import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

public class InspectionData {
    private long ID;
    private boolean queenCellsPresent;
    private boolean queenBeePresent;
    private boolean broodEggs;
    private boolean broodLarvae;
    private boolean broodCapped;
    private int numFramesBrood;
    private int stores;
    private boolean room;
    private boolean varroaSeen;
    private boolean varroaTreatment;
    private String beeTemper;
    private boolean feedGiven;
    private int supersAdded;

    private String weather;

    private String notes;

    private String date;

    // Constructors, one with and without the ID in case needed
    InspectionData(){
    }
    InspectionData(long ID){
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public boolean getQueenCellsPresent() {
        return queenCellsPresent;
    }

    public void setQueenCellsPresent(boolean queenCellsPresent) {
        this.queenCellsPresent = queenCellsPresent;
    }

    public boolean getQueenBeePresent() {
        return queenBeePresent;
    }

    public void setQueenBeePresent(boolean queenBeePresent) {
        this.queenBeePresent = queenBeePresent;
    }

    public boolean getBroodEggs() {
        return broodEggs;
    }

    public void setBroodEggs(boolean broodEggs) {
        this.broodEggs = broodEggs;
    }

    public boolean getBroodLarvae() {
        return broodLarvae;
    }

    public void setBroodLarvae(boolean broodLarvae) {
        this.broodLarvae = broodLarvae;
    }

    public boolean getBroodCapped() {
        return broodCapped;
    }

    public void setBroodCapped(boolean broodCapped) {
        this.broodCapped = broodCapped;
    }

    public int getNumFramesBrood() {
        return numFramesBrood;
    }

    public void setNumFramesBrood(int numFramesBrood) {
        this.numFramesBrood = numFramesBrood;
    }

    public int getStores() {
        return stores;
    }

    public void setStores(int stores) {
        this.stores = stores;
    }

    public boolean getRoom() {
        return room;
    }

    public void setRoom(boolean room) {
        this.room = room;
    }

    public boolean getVarroaSeen() {
        return varroaSeen;
    }

    public void setVarroaSeen(boolean varroaSeen) {
        this.varroaSeen = varroaSeen;
    }

    public boolean getVarroaTreatment() {
        return varroaTreatment;
    }

    public void setVarroaTreatment(boolean varroaTreatment) {
        this.varroaTreatment = varroaTreatment;
    }

    public String getBeeTemper() {
        return beeTemper;
    }

    public void setBeeTemper(String beeTemper) {
        this.beeTemper = beeTemper;
    }

    public boolean getFeedGiven() {
        return feedGiven;
    }

    public void setFeedGiven(boolean feedGiven) {
        this.feedGiven = feedGiven;
    }

    public int getSupersAdded() {
        return supersAdded;
    }

    public void setSupersAdded(int supersAdded) {
        this.supersAdded = supersAdded;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
