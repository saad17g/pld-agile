package models;

import observer.Observable;

import java.util.LinkedList;

public class Courier extends Observable {
    private int id;
    private String name;
    LinkedList <Request> requests;
    private Tour tour;

    /**
     * Create a Courier with an id
     * @param id
     */
    public Courier (int id){
        this.id = id;
        this.requests=new LinkedList<>();
    }
    /**
     * Add a Request to a Courier
     * @param r the Request
     */
    public void addRequest (Request r){
        this.requests.add(r);
    }
    /**
     * Selete a Request of a Courier
     * @param r the Request
     */
    public void removeRequest (Request r){requests.remove(r);}

    public LinkedList<Request> getRequests() {
        return requests;
    }
    public void setTour(Tour t){
        this.tour=t;
    }


    public String getName() {
        return name;
    }



    public void setName(String name) {
        this.name = name;
    }


    public String toString (){
        return "Courier "+ id + " : ("  + requests.size() +" requests ): \n";
    }
    public void display(Visitor v){
        v.display(this);
    }

    public boolean hasTour() {
        return tour != null;
    }

    public Tour getTour() {
        return tour;
    }

    /**
     * Clear all requests of this Courier.
     */
    public void clearRequests() {
        this.requests.clear();
    }
}
