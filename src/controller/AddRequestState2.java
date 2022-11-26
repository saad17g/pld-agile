package controller;

import models.Intersection;
import models.Map;
import models.Request;
import models.TimeWindow;
import view.Window;

import java.awt.*;

public class AddRequestState2 implements State{

    private Request request;
    @Override
    public void chooseCourier(Controller controller, Window window, ListOfCommands listOfCommands) {
        window.enableButtons("Cancel");
        window.displayMessage("Add a request: [Left Click] on the Map to select your <br/> delivery point. <br/> Click on [Cancel] to cancel this operation.");
        controller.addRequestState3.entryAction(window.getSelectedCourier(),this.request);
        controller.setCurrentState(controller.addRequestState3);
    }
    @Override
    public void addCourier (Controller c, Window w){
        w.addCourierButtons();
    }
    @Override
    public void cancel(Controller c, Window w, Map m){
        w.deselectRButtons("courier");
        w.changeVisibilityRButtonsAddCourier();
        w.changeBtnLabelAndColor("Cancel", "Add Request", Color.getColor(""));
        w.displayMessage("Operation cancelled !");
        w.deselectRButtons("add");
        w.switchRButtonsAddRequest();
        if(m.getCouriers().size()==0) {
            w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Undo", "Redo");
        } else {
            w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo","Compute Tour");
        }
        c.setCurrentState(c.loadedMapState);
    };
    public void warn (Controller c, Window w, String message){
        w.displayMessage(message);
    }
    protected void entryAction(TimeWindow tw){
        request = new Request();
        this.request.setTimeWindow(tw);
    }
}