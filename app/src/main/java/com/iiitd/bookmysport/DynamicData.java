package com.iiitd.bookmysport;

import java.util.ArrayList;

public class DynamicData {

    private ArrayList<String> data;


    public DynamicData(){

    }

    public DynamicData(ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "DynamicData{" +
                "data=" + data +
                '}';
    }

    public GuardItemInfo getGaurdItemInfo(int pos, String sportsName){
        String[] ar = data.get(pos).split("_");
        String startTime = ar[0];
        String endTime = ar[1];
        String TableNo = ar[2];
        String BookedType = ar[3];
        String rollnos = ar[4].split("@")[0];
        String hasConfirmed = ar[5];
         return new GuardItemInfo(sportsName, startTime, endTime, TableNo, BookedType, rollnos, hasConfirmed);
    }
}
