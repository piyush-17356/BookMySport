package com.iiitd.bookmysport;


public class model_myBookings {

    private String SportName;
    private String startTime;
    private String endTime;
    private String TableNo;
    private String BookedType;

    public model_myBookings() {

    }

    public model_myBookings(String details) {
        String[] ar = details.split("_");
        SportName = ar[2];
        startTime = ar[0];
        endTime = ar[1];
        TableNo = ar[3];
        BookedType = ar[4];
    }

    public model_myBookings(String sportName, String date, String time) {
        SportName = sportName;
        startTime = date;
        endTime = time;
    }

    public String getSportName() {
        return SportName;
    }

    public void setSportName(String sportName) {
        SportName = sportName;
    }

    public String getDate() {
        return startTime;
    }

    public void setDate(String date) {
        startTime = date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTableNo() {
        return TableNo;
    }

    public void setTableNo(String tableNo) {
        TableNo = tableNo;
    }

    public String getBookedType() {
        return BookedType;
    }

    public void setBookedType(String bookedType) {
        BookedType = bookedType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @Override
    public String toString() {
        return "model_myBookings{" +
                "SportName='" + SportName + '\'' +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", TableNo='" + TableNo + '\'' +
                ", BookedType='" + BookedType + '\'' +
                '}';
    }

    public String getStringInFirebaseFormat(){
        return this.startTime + "_" + this.endTime + "_" + this.SportName + "_" + this.TableNo + "_" + this.BookedType;
    }
}
