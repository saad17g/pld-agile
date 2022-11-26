package models;

import observer.Observable;
import xml.XMLDeserializer;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Map extends Observable  {
    private HashMap<Long, ArrayList<Segment>> edges;
    private LinkedList<Long> listIds;
    private final HashMap<Long, Intersection> listIntersections;
    private double latMax,latMin,lonMax,lonMin;
    private final LinkedList<Tour> tours;
    private final LinkedList<Request> requests; //displaying request's information
    private Intersection warehouse;
    private final HashMap<Integer,Courier> couriers;

    /**
     * Create a new map
     */
    public Map() {
        IntersectionFactory.initFactory();
        this.edges = new HashMap<>();
        this.listIntersections = new HashMap<>();
        this.listIds = new LinkedList<>();
        requests = new LinkedList<>();
        tours=new LinkedList<>();
        this.couriers=new HashMap<>();
    }
    private void updateCorners(){
        latMax = IntersectionFactory.getLatMax();
        latMin = IntersectionFactory.getLatMin();
        lonMax = IntersectionFactory.getLonMax();
        lonMin = IntersectionFactory.getLonMin();
    }
    public HashMap<Integer, Courier> getCouriers() {return couriers;}

    public void fillMap () throws Exception {
        try {
            XMLDeserializer deserializer = new XMLDeserializer();
            deserializer.open();
            deserializer.deserialize(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                null,
                "An error occurred when loading the map, please check if the file you chose is correct.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
            updateCorners();
            this.notifyObservers();
            throw e;
        }
        updateCorners();
        this.notifyObservers();
    }

    public Intersection getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public double getLatMax() {
        return latMax;
    }
    public double getLatMin() {
        return latMin;
    }
    public double getLonMax(){
        return lonMax;
    }

    public double getLonMin() {
        return lonMin;
    }
    public LinkedList<Long> getListIds(){
        return listIds;
    }
    public ArrayList<Segment> getEdges(Long id) {
        return this.edges.get(id);
    }
    public int numberOfNodes() {
        return this.edges.size();
    }
    public HashMap<Long, Intersection> getlistIntersections(){
        return this.listIntersections;
    }
    public LinkedList<Tour> getTours(){
        return tours;
    }

    /**
     * Add a delivery point (Request)
     * @param r
     */
    public void addDeliveryPoint (Request r){
        int id = r.getIdCourier();
        if(!couriers.containsKey(id)){
            couriers.put(id,new Courier(id));
        }
        couriers.get(id).addRequest(r);
        requests.add(r);
        notifyObservers(r);
    }

    /**
     * Remove a delivery point (Request)
     * @param r
     */
    public void removeDeliveryPoint (Request r){
        Integer idC = r.getIdCourier();
        couriers.get(idC).removeRequest(r);
        if(couriers.get(idC).getRequests().isEmpty()){
            couriers.remove(idC);
        }
        notifyObservers(r);
    }
    public void modifyDeliveryPoint(Request r){
        notifyObservers(r);
    }

    /**
     * Reset map
     */
    public void clear() {
        edges = new HashMap<>();
        listIds = new LinkedList<>();
        requests.clear();
        tours.clear();
        warehouse = null;

        for (java.util.Map.Entry<Integer, Courier> entry : this.couriers.entrySet()) {
            entry.getValue().clearRequests();
            entry.getValue().setTour(null);
            this.couriers.remove(entry.getKey());
        }

        IntersectionFactory.clear();
        updateCorners();
    }
    @Override
    public String toString() {
        return "Map{" +
                "edges=" + edges +
                '}';
    }
    public void addIntersection(Intersection intersection) {
        this.listIntersections.put(intersection.getId(),intersection);
    }
    public void addEdge(Long id, Segment s) {
        if (!this.edges.containsKey(id)) {this.edges.put(id, new ArrayList<>());}
        this.edges.get(id).add(s);
        listIds.add(id);
    }

    public Segment getSegment(Intersection i1, Intersection i2){
        //Get out segments from i1
        ArrayList<Segment> out = this.getEdges(i1.getId());
        //Find and return segments from i1 to 12 if it exists or return null
        for(Segment s : out){
            if(s.getEnd().getId() == i2.getId()){   //Found!
                return s;
            }
        }
        return null;
    }
    /**
     * Find a request associated with an intersection
     * @param i the intersection
     */
    public Request findRequestFromIntersection(Intersection i)
    {
        Request res = null;
        for(Request r:requests)
            if(r.getDestination()==i)
                res=r;
        return res;
    }
    //TODO: une façon meilleure de repérer une rqt à partir d'une intersection
    // pour que même si on clique un peu à côté de la rqt, elle soit repérée
    // idée: ajouter une tolérence de distance entre une requete et l'intersection cliquée
    /**
     * save the best tour here and then display it on the map view
     * @param t the Tour
     */
    public void addTour(Tour t){
        tours.add(t);
        notifyObservers(t);
    }
    /**
     * Update an already calculated tour
     * @param oldT the old Tour
     * @param newT the new Tour
     */
    public void updateTour(Tour oldT, Tour newT){
        tours.remove(oldT);
        tours.add(newT);
        notifyObservers(newT);
    }
}