package com.iiitd.bookmysport;

import com.iiitd.bookmysport.bookings.Document;
import com.iiitd.bookmysport.other.Functions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Time_Data {

    private int sportsID;
    private String sportsName;
    private int max;
    private ArrayList<Time_Slot> timeSlots;

    public Time_Data() {

    }

    public Time_Data(int sportsID) {
        this.sportsID = sportsID;
        this.sportsName = Functions.getSportsName(sportsID);
        this.timeSlots = new ArrayList<>();
        this.max = 0;
    }

    public void initialize(ArrayList<Document> documents) {

        this.timeSlots = new ArrayList<>();
        this.max = Functions.getSportsMaxAvailability(this.sportsID);

        String time_slot_name = "";
        int occupied = 0;

        //SORT THE DOCUMENTS FIRST
        Collections.sort(documents, new Comparator<Document>() {
            @Override
            public int compare(Document document, Document t1) {
                return document.getId() - t1.getId();
            }
        });

        for (Document doc : documents) {
            time_slot_name = Functions.getTimeSlotNameFromID(doc.getId());
            occupied = doc.getOccupied();
            timeSlots.add(new Time_Slot(time_slot_name, occupied,doc.getId()));
        }

    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getSportsID() {
        return sportsID;
    }

    public void setSportsID(int sportsID) {
        this.sportsID = sportsID;
    }

    public String getSportsName() {
        return sportsName;
    }

    public void setSportsName(String sportsName) {
        this.sportsName = sportsName;
    }

    public ArrayList<Time_Slot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(ArrayList<Time_Slot> timeSlots) {
        this.timeSlots = timeSlots;
    }
}
