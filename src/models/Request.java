package models;

import observer.Observable;

public class Request extends Observable {
    private TimeWindow timeWindow;
    private Intersection destination;
    private TimeStamp deliveryTime;
    private Integer waitingTime = 0;
    private boolean delayed;
    private int idCourier;

    /**
     * Create an empty Request
     */
    public Request(){}

    /**
     * Create a Request with a Timewindow and a Courier
     * @param tw the Timewindow
     * @param id the Courier id
     */
    public Request (TimeWindow tw, int id){
        idCourier=id;
        timeWindow=tw;
    }

    /**
     * Copy constructor of Request
     * @param r
     */
    public Request (Request r){
        this.timeWindow = r.timeWindow;
        this.destination=r.destination;
        this.idCourier=r.idCourier;
    }

    /**
     * Create a Request with a Timewindow and destination
     * @param timeWindow
     * @param destination
     */
    public Request(TimeWindow timeWindow, Intersection destination) {
        this.timeWindow = timeWindow;
        this.destination = destination;
        this.delayed = false;
        this.waitingTime=0;
    }

    public TimeWindow getTimeWindow() {
        return timeWindow;
    }

    public void setTimeWindow(TimeWindow timeWindow) {
        this.timeWindow = timeWindow;
    }

    public void setDeliveryTime(TimeStamp deliveryTime) {
        this.deliveryTime = deliveryTime;
        if(deliveryTime.getHour()>timeWindow.getStart()){
            delayed = true;
        }
    }

    public TimeStamp getDeliveredTime() {
        return deliveryTime;
    }

    public Intersection getDestination() {
        return destination;
    }
    public void setWaitingTime(Integer waitingTime){
        this.waitingTime=waitingTime;
    }

    public Integer getWaitingTime() {
        return waitingTime;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    public void setIdCourier(int idCourier) {
        this.idCourier = idCourier;
    }

    public int getIdCourier() {
        return idCourier;
    }

    public void display(Visitor v){
        v.display(this);
    }
}
