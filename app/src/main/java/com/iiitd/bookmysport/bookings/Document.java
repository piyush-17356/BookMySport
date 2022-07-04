package com.iiitd.bookmysport.bookings;

import java.io.Serializable;
import java.util.ArrayList;

public class Document implements Serializable {

    private ArrayList<String> Rollnos;
    private int id;
    private int occupied;

    public int getOccupied() {
        return occupied;
    }

    public void setOccupied(int occupied) {
        this.occupied = occupied;
    }

    public Document(ArrayList<String> rollnos, int id, int occupied) {
        Rollnos = rollnos;
        this.id = id;
        this.occupied = occupied;
    }

    public Document() {

    }

    public ArrayList<String> getRollnos() {
        return Rollnos;
    }

    public void setRollnos(ArrayList<String> rollnos) {
        Rollnos = rollnos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Document{" +
                "Rollnos=" + Rollnos +
                ", id=" + id +
                ", occupied=" + occupied +
                '}';
    }
}
