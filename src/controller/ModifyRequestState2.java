package controller;

import models.Intersection;
import models.Map;
import models.Request;
import models.TimeWindow;
import view.Window;

import java.awt.*;
import java.util.Objects;

public class ModifyRequestState2 implements  State{
    private Request request;
    private Intersection oldInter;
    @Override
    public void leftClick(Controller controller, Window window, Map map, ListOfCommands listOfCommands, Intersection i) {
        oldInter = request.getDestination();
        request.setDestination(i);
        confirm(controller, window, map);
    }

    @Override
    public void confirm(Controller c, Window w, Map m){
        TimeWindow oldTw = request.getTimeWindow();
        request.setTimeWindow(w.getSelectedRButton("modify"));
        if(Objects.isNull(oldInter))
            oldInter = request.getDestination();

        m.modifyDeliveryPoint(request);
        w.changeBtnLabelAndColor("Confirm", "Modify Request", Color.getColor(""));
        w.deselectRButtons("modify");
        w.changeVisibilityRButtonsModifyRequest();
        w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo","Compute Tour");
        w.displayMessage("");
        c.setCurrentState(c.loadedMapState);
    }

    protected  void entryAction(Request r, ListOfCommands l){
        request = r;
    }
}
