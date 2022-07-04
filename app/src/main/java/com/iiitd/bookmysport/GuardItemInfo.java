package com.iiitd.bookmysport;

import java.util.Objects;

public class GuardItemInfo {
    String SportName;
    String Starttime;
    String Endtime;
    String tableNO;
    String bookingType;
    String Rollno;
    String hasConfirmed;

    public GuardItemInfo() {
    }

    public GuardItemInfo(String sportName, String starttime, String endtime, String tableNO, String bookingType, String rollno, String hasConfirmed) {
        SportName = sportName;
        Starttime = starttime;
        Endtime = endtime;
        this.tableNO = tableNO;
        this.bookingType = bookingType;
        Rollno = rollno;
        this.hasConfirmed = hasConfirmed;
    }

    public String getSportName() {
        return SportName;
    }

    public void setSportName(String sportName) {
        SportName = sportName;
    }

    public String getStarttime() {
        return Starttime;
    }

    public void setStarttime(String starttime) {
        Starttime = starttime;
    }

    public String getEndtime() {
        return Endtime;
    }

    public void setEndtime(String endtime) {
        Endtime = endtime;
    }

    public String getTableNO() {
        return tableNO;
    }

    public void setTableNO(String tableNO) {
        this.tableNO = tableNO;
    }

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public String getRollno() {
        return Rollno;
    }

    public void setRollno(String rollno) {
        Rollno = rollno;
    }

    public String getHasConfirmed() {
        return hasConfirmed;
    }

    public void setHasConfirmed(String hasConfirmed) {
        this.hasConfirmed = hasConfirmed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GuardItemInfo that = (GuardItemInfo) o;
        return SportName.equals(that.SportName) &&
                Starttime.equals(that.Starttime) &&
                Endtime.equals(that.Endtime) &&
                tableNO.equals(that.tableNO) &&
                bookingType.equals(that.bookingType) &&
                Rollno.equals(that.Rollno) &&
                hasConfirmed.equals(that.hasConfirmed);
    }

    @Override
    public int hashCode() {
        return Objects.hash(SportName, Starttime, Endtime, tableNO, bookingType, Rollno, hasConfirmed);
    }

    public String[] getDBMatchableString(){
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(this.Starttime);
        stringBuilder.append("_");
        stringBuilder.append(this.Endtime);
        stringBuilder.append("_");
        stringBuilder.append(this.tableNO);
        stringBuilder.append("_");
        stringBuilder.append(this.bookingType);
        stringBuilder.append("_");
        stringBuilder.append(this.Rollno + "@iiitd.ac.in");

        String[] ans = new String[3];
        ans[0] = this.SportName;
        ans[1] = stringBuilder.toString();
        ans[2] = "table" + this.tableNO;
        return ans;
    }
}
