package controller;

import models.Map;
import view.Window;

public class InitialState implements State{
    @Override
    public void loadMap(Controller c, Map m, Window w, ListOfCommands listOfCommands)
    {
        try {
            m.fillMap();
            w.enableButtons("Load Map", "Add Request","Save Requests", "Load Requests", "Undo", "Redo");
            c.setCurrentState(c.loadedMapState);
        } catch (Exception e) {}
        listOfCommands.reset();
    }
}
