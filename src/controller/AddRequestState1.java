package controller;

import models.Map;
import models.TimeWindow;
import view.Window;

import java.awt.*;

public class AddRequestState1 implements State{
    @Override
    public void chooseTimeWindow(Controller c, Window w, ListOfCommands l){
        w.enableButtons("Add Courier","Cancel");
        w.displayMessage("Choose a courier: [Left Click] on the box to select a courier.<br/>" +
                " [Left Click] on the button to add a new one. " +
                " <br/> Click on [Cancel] to cancel this operation.");
        w.changeVisibilityRButtonsAddCourier();
        c.addRequestState2.entryAction(w.getSelectedRButton("add"));
        c.setCurrentState(c.addRequestState2);
    }
    @Override
    public void cancel(Controller c, Window w, Map m){
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
}
