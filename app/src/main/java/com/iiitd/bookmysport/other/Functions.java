package com.iiitd.bookmysport.other;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Functions {

    public static int getTimeSlotIndex(String time) {
        int start = ((int) time.charAt(0) - 48) * 10 + ((int) time.charAt(1) - 48);
        String end2 = "";
        end2 += time.charAt(11);
        end2 += time.charAt(12);
        int x = 0;
        if (start == 12) {
            x = 1;
        } else {
            x = start + 1;
        }
        if (end2.equals("AM"))
            return x;
        else
            return x + 12;
    }

    public static String getSportsName(int type) {
        String s;
        switch (type) {
            case 0:
                s = "Badminton";
                break;
            case 1:
                s = "Basketball";
                break;
            case 2:
                s = "Pool";
                break;
            case 3:
                s = "Snooker";
                break;
            case 4:
                s = "Cricket";
                break;
            case 5:
                s = "Football";
                break;
            case 6:
                s = "Squash";
                break;
            case 7:
                s = "Swimming";
                break;
            default:
                s = "Null";
                break;
        }
        return s;
    }

    public static String getSportsDocName(int type) {
        return Functions.getSportsName(type) + "Doc";
    }

    public static String getTimeSlotNameFromID(int ID) {

        switch (ID) {
            case 1:
                return "12:00-01:00AM";
            case 2:
                return "01:00-02:00AM";
            case 3:
                return "02:00-03:00AM";
            case 4:
                return "03:00-04:00AM";
            case 5:
                return "04:00-05:00AM";
            case 6:
                return "05:00-06:00AM";
            case 7:
                return "06:00-07:00AM";
            case 8:
                return "07:00-08:00AM";
            case 9:
                return "08:00-09:00AM";
            case 10:
                return "09:00-10:00AM";
            case 11:
                return "10:00-11:00AM";
            case 12:
                return "11:00-12:00PM";
            case 13:
                return "12:00-01:00PM";
            case 14:
                return "01:00-02:00PM";
            case 15:
                return "02:00-03:00PM";
            case 16:
                return "03:00-04:00PM";
            case 17:
                return "04:00-05:00PM";
            case 18:
                return "05:00-06:00PM";
            case 19:
                return "06:00-07:00PM";
            case 20:
                return "07:00-08:00PM";
            case 21:
                return "08:00-09:00PM";
            case 22:
                return "09:00-10:00PM";
            case 23:
                return "10:00-11:00PM";
            case 24:
                return "11:00-12:00AM";
        }
        return "NULL";
    }

    public static int getSportsMaxAvailability(int type) {
        // IF CHANGING VALUES, CHANGE IT TO FIRESTORE ALSO!!
        switch (type) {
            case 0:
                //s = "Badminton";
                return 4;
            case 1:
                //s = "Basketball";
                return 10;
            case 2:
                //s = "Pool";
                return 4;
            case 3:
                //s = "Snooker";
                return 3;
            case 4:
                //s = "Cricket";
                return 22;
            case 5:
                //s = "Football";
                return 22;
            case 6:
                //s = "Squash";
                return 4;
            case 7:
                //s = "Swimming";
                return 6;
        }
        return 0;
    }

    public static int getCurrentTimeIndex(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String time = mdformat.format(calendar.getTime());
        //Log.d("tag","Time = " + time);

        int hr = Integer.parseInt(time.split(":")[0].trim());
        //Log.d("tag","HR = " + hr);

        return hr + 1;
    }




    public static int[] getBookedSlots(ArrayList<String> data){

        int[] time = new int[24*60];

        for(String s : data){

            String start = s.substring(0,7);
            String end = s.substring(8);

            int startHR = Integer.parseInt(start.substring(0,2).trim());
            int startMIN = Integer.parseInt(start.substring(3,5).trim());

            int endHR = Integer.parseInt(end.substring(0,2).trim());
            int endMIN = Integer.parseInt(end.substring(3,5).trim());

            int startTime = startHR * 60 + startMIN;
            int endTime = endHR * 60 + endMIN;

            Log.d("tag","start = " + startTime + "  end = " + endTime);

            for(int i = startTime;i<=endTime;i++){
                time[i] = 1;
            }



        }

        return time;
    }


    public static String getTimeFromInt(int time){
        int hr = time/60;
        int min = time%60;

        String ans = String.format("%02d:%02d", hr, min);
        return ans;
    }


    public static String getSportsNameDynamic(int type) {
        String s;
        switch (type) {
            case 0:
                s = "Swimming";
                break;
            case 1:
                s = "TableTennis";
                break;
            case 2:
                s = "Badminton";
                break;
            case 3:
                s = "Tennis";
                break;
            case 4:
                s = "Squash";
                break;
            case 5:
                s = "Football";
                break;
            case 6:
                s = "Basketball";
                break;
            case 7:
                s = "Volleyball";
                break;
            default:
                s = "Null";
                break;
        }
        return s;
    }




}
