package controller;

import algorithms.TourComputer;
import models.*;
import view.Window;

import java.awt.*;

public class AddRequestState4 implements State{

    private Request request;
    @Override
    public void leftClick(Controller controller, Window window, Map map, ListOfCommands listOfCommands, Intersection i) {
        Request before = map.findRequestFromIntersection(i);
        Courier courier = map.getCouriers().get(request.getIdCourier());
        if(before!=null) {
            //TourComputer tc = new TourComputer(map, request.getIdCourier());
            //Tour newTour = tc.addRequest(courier.getTour(),request,before);
            //Tour oldT = courier.getTour();
            //courier.setTour(newTour);
            //map.addDeliveryPoint(request);
            //map.updateTour(oldT,newTour);
            listOfCommands.add(new AddCommand(map, request,before));

            if(map.getCouriers().size()==0) {
                window.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
            } else {
                window.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo", "Compute Tour");
            }
            cancel(controller, window, map);
        }
    }
    @Override
    public void cancel(Controller c, Window w, Map m){
        w.deselectRButtons("courier");
        w.changeVisibilityRButtonsAddCourier();
        w.changeBtnLabelAndColor("Cancel", "Add Request", Color.getColor(""));
        w.displayMessage("");
        w.deselectRButtons("add");
        w.switchRButtonsAddRequest();
        if(m.getCouriers().isEmpty()) {
            w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
        } else {
            w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo","Compute Tour");
        }
        c.setCurrentState(c.loadedMapState);
    }

    @Override
    public void clickOnTextualRequest(Controller c, Window w, Map map, Request before, ListOfCommands l){
        Courier courier = map.getCouriers().get(request.getIdCourier());
        if(before!=null) {
            //TourComputer tc = new TourComputer(map, request.getIdCourier());
            //Tour newTour = tc.addRequest(courier.getTour(),request,before);
            //Tour oldT = courier.getTour();
            //courier.setTour(newTour);
            //map.addDeliveryPoint(request);
            //map.updateTour(oldT,newTour);
            l.add(new AddCommand(map, request,before));

            if(map.getCouriers().size()==0) {
                w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
            } else {
                w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo", "Compute Tour");
            }
            cancel(c, w, map);
        }
    }
    public void warn (Controller c, Window w, String message){
        w.displayMessage(message);
    }
    protected void entryAction(Request r){
        request = new Request(r);
    }

}