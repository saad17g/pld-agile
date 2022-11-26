package models;

import java.sql.Time;

public class TimeStamp {
    private Integer hour;
    private Integer minute;

    /**
     * Create a TimeStamp
     * @param hour
     * @param minute
     */
    public TimeStamp(Integer hour, Integer minute){
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Copy constructor of TimeStamp
     * @param ts
     */
    public TimeStamp (TimeStamp ts){
        this.hour=ts.getHour();
        this.minute=ts.getMinute();
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }
    public void add(Integer newMinute){
        if(minute+newMinute >=60){
            hour++;
            minute=minute+newMinute-60;
        } else minute=minute+newMinute;
    }
    @Override
    public String toString(){
        return hour + ":" +minute;
    }
}
