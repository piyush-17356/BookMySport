package com.iiitd.bookmysport;

//SPORTSID_SLOTTIMENAME_DAY  DAy = 1:TOday,2:Tom


import com.iiitd.bookmysport.other.Functions;

public class BookingInfo {

    private int sportsID;
    private String sportsName;
    private String slotName;
    private int day;

    BookingInfo() {

    }

    public BookingInfo(int sportsID, String slotName, int day) {
        this.sportsID = sportsID;
        this.slotName = Functions.getSportsName(sportsID);
        this.slotName = slotName;
        this.day = day;
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

    public String getSlotName() {
        return slotName;
    }

    public void setSlotName(String slotName) {
        this.slotName = slotName;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
