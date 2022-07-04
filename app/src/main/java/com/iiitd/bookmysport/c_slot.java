package com.iiitd.bookmysport;

import com.iiitd.bookmysport.other.Functions;

import java.util.ArrayList;

public class c_slot{

    String c_time;
    ArrayList<String> uc_left;
    ArrayList<String> uc_right;

    public c_slot(int[] left, int[] right, int[] merge, String c, int start, int end) {

        this.c_time = c;
        uc_left = new ArrayList<>();
        uc_right = new ArrayList<>();

        int si = -1;
        int ei = -1;
        for(int i=start;i<=end;) {
            if (left[i] == 1) {
                si = i;
                while (left[i] == 1) {
                    i++;
                }
                ei = i - 1;
                String st = Functions.getTimeFromInt(si);
                String en = Functions.getTimeFromInt(ei);
                uc_left.add(st + "_" + en);
            } else {
                i++;
            }
        }

        si = -1;
        ei = -1;

        for(int i=start;i<=end;) {
            if (right[i] == 1) {
                si = i;
                while (right[i] == 1) {
                    i++;
                }
                ei = i - 1;
                String st = Functions.getTimeFromInt(si);
                String en = Functions.getTimeFromInt(ei);
                uc_right.add(st + "_" + en);
            } else {
                i++;
            }
        }


    }

    public String getC_time() {
        return c_time;
    }

    public void setC_time(String c_time) {
        this.c_time = c_time;
    }

    public ArrayList<String> getUc_left() {
        return uc_left;
    }

    public void setUc_left(ArrayList<String> uc_left) {
        this.uc_left = uc_left;
    }

    public ArrayList<String> getUc_right() {
        return uc_right;
    }

    public void setUc_right(ArrayList<String> uc_right) {
        this.uc_right = uc_right;
    }

}