package controller;

import algorithms.TourComputer;
import models.*;
import view.Window;

import java.awt.*;
import java.util.LinkedList;

public class AddRequestState3 implements State{

    private Request request;
    @Override
    public void leftClick(Controller controller, Window window, Map map, ListOfCommands listOfCommands, Intersection i) {
        request.setDestination(i);
        Courier courier = map.getCouriers().get(request.getIdCourier());
        if(courier != null && courier.hasTour()){
            window.enableButtons("Cancel");
            window.displayMessage("Add a request: [Left Click] on the Map or in the list to select the previous delivery point <br/> Click on [Cancel] to cancel this operation.");
            controller.addRequestState4.entryAction(this.request);
            controller.setCurrentState(controller.addRequestState4);
        }else{
            listOfCommands.add(new AddCommand(map, request, null));
            window.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo", "Compute Tour");
            window.displayMessage("A new request is added successfully!");
            // going back to initial state
            controller.cancel();
            controller.setCurrentState(controller.loadedMapState);
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
    public void warn (Controller c, Window w, String message){
        w.displayMessage(message);
    }
    protected void entryAction(int id, Request r){
        request = new Request(r);
        request.setIdCourier(id);

    }

}