package controller;

import models.Intersection;
import models.Map;
import models.Request;
import view.Window;

import javax.swing.*;
import java.awt.*;

public class ModifyRequestState1 implements  State{
    @Override
    public void leftClick(Controller controller, Window window, Map map, ListOfCommands listOfCommands, Intersection i) {
        Request r = map.findRequestFromIntersection(i);
        if(r!=null)
        {
            String message = "You selected the request at the coordinates :"+
                    r.getDestination().getLatitude()+" "+
                    r.getDestination().getLongitude()+
                    " with the time window of : "+
                    r.getTimeWindow().getStart()+" - "+
                    r.getTimeWindow().getEnd()+"<br/>"+
                    "You can change the time-window then click on [Confirm] to finish the modification <br/>"+
                    "Or directly select your new delivery point in the map <br/>"+
                    "Or change the time-window then select a new delivery point";
            window.displayMessage(message);
            window.changeBtnLabelAndColor("Cancel", "Confirm", Color.green);
            window.changeVisibilityRButtonsModifyRequest();
            //fill the right rbutton
            for(JRadioButton rbtn : window.getrButtonsModifyRequest()){
                if(rbtn.getText().substring(0, rbtn.getText().indexOf("-")).equals(r.getTimeWindow().getStart().toString())){
                    rbtn.setSelected(true);
                }
            }
            controller.modifyRequestState2.entryAction(r, listOfCommands);
            controller.setCurrentState(controller.modifyRequestState2);
        }
    }
    @Override
    public void clickOnTextualRequest(Controller c, Window w, Map m, Request r, ListOfCommands l){
        if(r!=null)
        {
            String message = "You selected the request at the coordinates :"+
                    r.getDestination().getLatitude()+" "+
                    r.getDestination().getLongitude()+
                    " with the time window of : "+
                    r.getTimeWindow().getStart()+" - "+
                    r.getTimeWindow().getEnd()+"<br/>"+
                    "You can change the time-window then click on [Confirm] to finish the modification <br/>"+
                    "Or directly select your new delivery point in the map <br/>"+
                    "Or change the time-window then select a new delivery point";
            w.displayMessage(message);
            w.changeBtnLabelAndColor("Cancel", "Confirm", Color.green);
            w.changeVisibilityRButtonsModifyRequest();
            //fill the right rbutton
            for(JRadioButton rbtn : w.getrButtonsModifyRequest()){
                if(rbtn.getText().substring(0, rbtn.getText().indexOf("-")).equals(r.getTimeWindow().getStart().toString())){
                    rbtn.setSelected(true);
                }
            }
            c.modifyRequestState2.entryAction(r, l);
            c.setCurrentState(c.modifyRequestState2);
        }
    }
    @Override
    public void cancel(Controller c, Window w, Map m){
        w.changeBtnLabelAndColor("Cancel", "Modify Request", Color.getColor(""));
        w.displayMessage("");
        w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo","Compute Tour");
        c.setCurrentState(c.loadedMapState);
    };
}
