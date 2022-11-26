package controller;

import algorithms.TourComputer;
import models.*;
import view.Window;

import java.awt.*;

public class DeleteRequestState implements State{
    @Override
    public void leftClick(Controller controller, Window window, Map map, ListOfCommands listOfCommands, Intersection i) {
        Request r = map.findRequestFromIntersection(i);
        if(r!=null)
        {
            Courier courier = map.getCouriers().get(r.getIdCourier());
            if(courier.hasTour()){
                //TourComputer tc = new TourComputer(map,r.getIdCourier());
                //Tour newTour = tc.deleteRequest(courier.getTour(),r);
                //Tour oldT = courier.getTour();
                //courier.setTour(newTour);
                //map.removeDeliveryPoint(r);
                //map.updateTour(oldT,newTour);
                int index = courier.getTour().getOrderedRequests().indexOf(r);
                Request before = null;
                if (index != 0) {
                    before = courier.getTour().getOrderedRequests().get(index - 1);
                }
                listOfCommands.add(new ReverseCommand(new AddCommand(map, r,before)));
            }else{
                listOfCommands.add(new ReverseCommand(new AddCommand(map,r,null)));
            }
            if(map.getCouriers().size()==0) {
                window.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
            } else {
                window.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo", "Compute Tour");
            }
            cancel(controller, window, map);
        }
    }

    @Override
    public void clickOnTextualRequest(Controller c, Window w, Map map, Request r, ListOfCommands l){
        if(r!=null)
        {
            Courier courier = map.getCouriers().get(r.getIdCourier());
            if(courier.hasTour()){
                //TourComputer tc = new TourComputer(map,r.getIdCourier());
                //Tour newTour = tc.deleteRequest(courier.getTour(),r);
                //Tour oldT = courier.getTour();
                //courier.setTour(newTour);
                //map.removeDeliveryPoint(r);
                //map.updateTour(oldT,newTour);
                int index = courier.getTour().getOrderedRequests().indexOf(r);
                Request before = null;
                if (index != 0) {
                    before = courier.getTour().getOrderedRequests().get(index - 1);
                }
                l.add(new ReverseCommand(new AddCommand(map, r,before)));
            }else {
                l.add(new ReverseCommand(new AddCommand(map, r,null)));
            }
            if(map.getCouriers().size()==0) {
                w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
            } else {
                w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo", "Compute Tour");
            }
            cancel(c, w, map);
        }
    }

    @Override
    public void cancel(Controller c, Window w, Map m){
        w.changeBtnLabelAndColor("Cancel", "Delete Request", Color.getColor(""));
        w.displayMessage("");
        if(m.getCouriers().size()==0) {
            w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
        } else {
            w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo", "Compute Tour");
        }
        c.setCurrentState(c.loadedMapState);
    }
}
