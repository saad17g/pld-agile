package controller;

import algorithms.TourComputer;
import models.*;

public class AddCommand implements Command {

    private final Request request;
    private final Map map;
    private final Request before;

    /**
     * Create the command which adds the shape s to the plan p
     * @param m the map to which r is added
     * @param r the delivery request added to map
     */
    public AddCommand(Map m, Request r, Request before){
        this.map = m;
        this.request = r;
        this.before = before;
    }

    @Override
    public void doCommand() {
        Courier courier = map.getCouriers().get(request.getIdCourier());
        if(courier != null && courier.hasTour() && before != null){
            //Request before = map.findRequestFromIntersection(i);
            TourComputer tc = new TourComputer(map, request.getIdCourier());
            Tour newTour = tc.addRequest(courier.getTour(),request,before);
            Tour oldT = courier.getTour();
            courier.setTour(newTour);
            map.addDeliveryPoint(request);
            map.updateTour(oldT,newTour);
        }
        else {
            map.addDeliveryPoint(request);
        }
    }

    @Override
    public void undoCommand() {
        Courier courier = map.getCouriers().get(request.getIdCourier());
        if(courier != null && courier.hasTour()){
            TourComputer tc = new TourComputer(map,request.getIdCourier());
            Tour newTour = tc.deleteRequest(courier.getTour(),request);
            Tour oldT = courier.getTour();
            courier.setTour(newTour);
            map.removeDeliveryPoint(request);
            map.updateTour(oldT,newTour);
        }
        else{
            map.removeDeliveryPoint(request);
        }
    }

}
