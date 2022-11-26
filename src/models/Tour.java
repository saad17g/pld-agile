package models;

import observer.Observable;

import java.util.Iterator;
import java.util.LinkedList;

public class Tour extends Observable {
    private Courier courier;
    private LinkedList<Itinerary> orderedItineraries;
    private LinkedList <Request> orderedRequests;
    private Iterator<Itinerary> itineraryIterator;
    private Iterator<Request> requestIterator;
    private final Integer HOUR_START_AT_WAREHOUSE = 8;
    private final Integer DELIVERY_TIME = 5;
    public Tour (){
        orderedItineraries = new LinkedList<Itinerary>();
    }

    /**
     * Create a Tour from ordered Itiniaries and Requests
     * @param orderedItineraries
     * @param orderedRequests
     */
    public Tour (LinkedList <Itinerary> orderedItineraries, LinkedList<Request>orderedRequests){
        this.orderedItineraries = new LinkedList<Itinerary>();
        this.orderedRequests = new LinkedList<>();
        itineraryIterator = orderedItineraries.iterator();
        requestIterator = orderedRequests.iterator();
        for(Itinerary i: orderedItineraries){
            this.orderedItineraries.add(i);
        }
        for (Request r:orderedRequests){
            this.orderedRequests.add(r);
        }
    }

    public LinkedList<Itinerary> getOrderedItineraries() {
        return orderedItineraries;
    }

    public LinkedList<Request> getOrderedRequests() {
        return orderedRequests;
    }

    @Override
    public String toString(){
        int nbDeliveries = 0;
        String result="";
        while (itineraryIterator.hasNext() && requestIterator.hasNext()){
            result += "Delivery "+ nbDeliveries + ": "+itineraryIterator.next();
            Request r = requestIterator.next();
            if(r.getWaitingTime()>0){
                result += " Waiting time at the delivery point: "+r.getWaitingTime()+ " minutes.";
            }
            result += " Delivered time of the request: "+r.getDeliveredTime()+".\n";
            nbDeliveries++;
        }
        return result;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public void display(Visitor v){
        v.display(this);
    }

    /**
     * Calculate delivery time and update delivery time in the Requests
     */
    public void calculateDeliveryTime(){
        LinkedList<Itinerary> itineraries = this.getOrderedItineraries();
        LinkedList<Request> requests = this.getOrderedRequests();

        TimeStamp currentTime = new TimeStamp(HOUR_START_AT_WAREHOUSE,0);
        for(int i=0; i<itineraries.size()-1;i++){   //we ignore return to warehouse
            Itinerary itinerary = itineraries.get(i);
            Request associatedRequest = requests.get(i);

            TimeStamp departureTime = new TimeStamp(currentTime);
            itinerary.setDepartureTime(departureTime);
            currentTime.add(itinerary.getDeplacementTimeInMinutes());
            TimeStamp arrivalTime = new TimeStamp(currentTime);
            itinerary.setArrivalTime(arrivalTime);

            if(associatedRequest.getTimeWindow().getStart()>arrivalTime.getHour()){
                //If the courier arrives before the time window, set current time to the time window of request
                currentTime = new TimeStamp(associatedRequest.getTimeWindow().getStart(),0);
                Integer differenceHour = associatedRequest.getTimeWindow().getStart()-arrivalTime.getHour();
                associatedRequest.setWaitingTime((differenceHour)*60-arrivalTime.getMinute());
            }
            currentTime.add(DELIVERY_TIME);
            TimeStamp deliveryTime = new TimeStamp(currentTime);
            associatedRequest.setDeliveryTime(deliveryTime);
        }
    }
}
