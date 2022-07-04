package com.iiitd.bookmysport;

import com.iiitd.bookmysport.other.Functions;

import java.util.ArrayList;

public class SportsSDData {

    private ArrayList<String> table1;
    private ArrayList<String> table2;
    private int[] table1Time;
    private int[] table2Time;
    private int[] mergeTime;
    private int count;
    private ArrayList<String> mergedSlots;
    private ArrayList<c_slot> c_slots;

    public ArrayList<String> getMergedSlots() {
        return mergedSlots;
    }

    public void setMergedSlots(ArrayList<String> mergedSlots) {
        this.mergedSlots = mergedSlots;
    }

    public SportsSDData(ArrayList<String> table1, ArrayList<String> table2) {
        this.table1 = table1;
        this.table2 = table2;
        table1Time = Functions.getBookedSlots(table1);
        table2Time = Functions.getBookedSlots(table2);
        mergedSlots = new ArrayList<>();
        c_slots = new ArrayList<>();

        mergeTime = new int[24*60];
        count = 0;
        merge();

        count = count();

    }

    private void merge(){

        int initial = -1;
        int end = -1;

        for (int i=0;i<24*60;i++){
            if(table1Time[i] == 1 || table2Time[i] == 1){
                mergeTime[i] = 1;
            }
            else{
                mergeTime[i] = 0;
            }

        }

    }



    private int count(){

        int count = 0;

        int start = -1;
        int end = -1;

        for (int i=0;i<24*60;){

            if(mergeTime[i] == 1){
                start = i;
                while(mergeTime[i] == 1) {
                    i++;
                }
                count++;
                end = i-1;


                String st = Functions.getTimeFromInt(start);
                String en = Functions.getTimeFromInt(end);

                mergedSlots.add(st + "_" + en);
                c_slot cs = new c_slot(table1Time,table2Time, mergeTime, st+"_"+en,start, end);
                c_slots.add(cs);

            }
            else{
                i++;
            }

        }

        return count;
    }


    public ArrayList<String> getTable1() {
        return table1;
    }

    public void setTable1(ArrayList<String> table1) {
        this.table1 = table1;
    }

    public ArrayList<String> getTable2() {
        return table2;
    }

    public void setTable2(ArrayList<String> table2) {
        this.table2 = table2;
    }

    public int[] getTable1Time() {
        return table1Time;
    }

    public void setTable1Time(int[] table1Time) {
        this.table1Time = table1Time;
    }

    public int[] getTable2Time() {
        return table2Time;
    }

    public void setTable2Time(int[] table2Time) {
        this.table2Time = table2Time;
    }

    public int[] getMergeTime() {
        return mergeTime;
    }

    public void setMergeTime(int[] mergeTime) {
        this.mergeTime = mergeTime;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<c_slot> getC_slots() {
        return c_slots;
    }

    public void setC_slots(ArrayList<c_slot> c_slots) {
        this.c_slots = c_slots;
    }
}

