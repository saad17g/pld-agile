package controller;

import models.Request;
import view.Window;
import models.Map;
import models.Intersection;

public interface State {
    /**
     * Method called by the controller after a click on the button "Add a request"
     * @param c the controller
     * @param w the window
     */
    public default void addRequest(Controller c, Window w){};
    public default void cancel(Controller c, Window w, Map m){};
    public default void confirm(Controller c, Window w, Map m){};
    public default void deleteRequest(Controller c, Window w){};
    public default void modifyRequest(Controller c, Window w){};
    public default void loadMap(Controller c, Map m, Window w, ListOfCommands listOfCommands){};
    public default void addCourier(Controller c, Window w){};
    /**
     * Method called by the controller when the mouse is moved on the graphical view
     * Precondition : p != null
     * @param map the plan
     * @param i the point corresponding to the mouse position
     */
    public default void mouseMoved(Map map, Intersection i){};


    /**
     * Method called by the controller after a right click
     * @param c the controler
     * @param w the window
     * @param l the current list of commands
     */
    public default void rightClick(Controller c, Window w, ListOfCommands l){
    }

    /**
     * Method called by the controller after a left click
     * Precondition : p != null
     * @param c the controler
     * @param w the window
     * @param map the plan
     * @param l the current list of commands
     * @param i the coordinates of the mouse
     */

    public default void leftClick(Controller c, Window w, Map map, ListOfCommands l, Intersection i){};
    public default void clickOnTextualRequest(Controller c, Window w, Map m, Request r, ListOfCommands l){};
    public default void chooseTimeWindow(Controller c, Window w, ListOfCommands l){};
    public default void chooseCourier(Controller c, Window w, ListOfCommands l){};
    public default void warn (Controller c, Window w, String warningMessage){};
    public default void computeTour(Controller c, Window w, Map map, ListOfCommands listOfCommands){};
    public default void generateRoadmap(Controller c, Window w, Map map){};
    /**
     * Method called by the controller after a click on the button "Undo"
     * @param l the current list of commands
     */
    public default void undo(ListOfCommands l){};

    /**
     * Method called by the controller after a click on the button "Redo"
     * @param l the current list of commands
     */
    public default void redo(ListOfCommands l){};
    
    /**
     * Load all requests from a file.
     */
    default void loadRequests(Window window, Map map, ListOfCommands listOfCommands){};

    /**
     * Save all requests to a file on disk.
     */
    default void saveRequests(Map map){};
}

