package com.iiitd.bookmysport;

import java.util.ArrayList;

public class SwimmingData {

    private String slotTime;
    private int slotID;
    private int occupied;
    private int remaining;
    private ArrayList<String> rollnos;
    private String timeU;

    public SwimmingData(){

    }

    public SwimmingData(String slotTime, int slotID, int occupied, int remaining, ArrayList<String> rollnos, String timeU) {
        this.slotTime = slotTime;
        this.slotID = slotID;
        this.occupied = occupied;
        this.remaining = remaining;
        this.rollnos = rollnos;
        this.timeU = timeU;
    }

    public String getSlotTime() {
        return slotTime;
    }

    public void setSlotTime(String slotTime) {
        this.slotTime = slotTime;
    }

    public int getSlotID() {
        return slotID;
    }

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public ArrayList<String> getRollnos() {
        return rollnos;
    }

    public void setRollnos(ArrayList<String> rollnos) {
        this.rollnos = rollnos;
    }

    public String getTimeU() {
        return timeU;
    }

    public void setTimeU(String timeU) {
        this.timeU = timeU;
    }

    @Override
    public String toString() {
        return "SwimmingData{" +
                "slotTime='" + slotTime + '\'' +
                ", slotID=" + slotID +
                ", occupied=" + occupied +
                ", remaining=" + remaining +
                '}';
    }
}
