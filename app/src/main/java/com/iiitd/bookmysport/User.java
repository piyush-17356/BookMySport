package com.iiitd.bookmysport;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private String id;
    private ArrayList<String> bookings;

    public User() {

    }

    public User(String id, ArrayList<String> bookings) {
        this.id = id;
        this.bookings = bookings;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<String> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<String> bookings) {
        this.bookings = bookings;
    }
}
