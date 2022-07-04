package com.iiitd.bookmysport;

public class Time_Slot {
    private String time_slot_name;
    private int occupied;
    private int time_slot_id;

    public Time_Slot() {

    }

    public Time_Slot(String time_slot_name, int occupied, int time_slot_id) {
        this.time_slot_name = time_slot_name;
        this.occupied = occupied;
        this.time_slot_id = time_slot_id;
    }

    public int getTime_slot_id() {
        return time_slot_id;
    }

    public void setTime_slot_id(int time_slot_id) {
        this.time_slot_id = time_slot_id;
    }

    public String getTime_slot_name() {
        return time_slot_name;
    }

    public void setTime_slot_name(String time_slot_name) {
        this.time_slot_name = time_slot_name;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }
}
