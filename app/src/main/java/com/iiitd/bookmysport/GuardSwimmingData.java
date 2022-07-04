package com.iiitd.bookmysport;

import java.util.ArrayList;

public class GuardSwimmingData {

    private ArrayList<String> paid;

    public GuardSwimmingData(ArrayList<String> paid) {
        this.paid = paid;
    }

    public GuardSwimmingData() {
    }

    public ArrayList<String> getPaid() {
        return paid;
    }

    public void setPaid(ArrayList<String> paid) {
        this.paid = paid;
    }
}
