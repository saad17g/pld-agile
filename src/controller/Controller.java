package controller;

import models.Intersection;
import models.Map;
import models.Request;
import view.Window;

public class Controller {
    private final Map map;
    private final Window window;
    private final ListOfCommands listOfCommands;
    private State currentState;
    protected  final InitialState initialState = new InitialState();
    protected final LoadedMapState loadedMapState = new LoadedMapState();
    protected final AddRequestState1 addRequestState1 = new AddRequestState1();
    protected final AddRequestState2 addRequestState2 = new AddRequestState2();
    protected final AddRequestState3 addRequestState3 = new AddRequestState3();
    protected final AddRequestState4 addRequestState4 = new AddRequestState4();
    protected final DeleteRequestState deleteRequestState = new DeleteRequestState();
    protected final ModifyRequestState1 modifyRequestState1 = new ModifyRequestState1();
    protected final ModifyRequestState2 modifyRequestState2 = new ModifyRequestState2();

    /**
     * Create the controller of the application
     * @param map the map
     * @param scale the graphical view scale
     */
    public Controller(Map map, int scale) {
        this.map = map;
        listOfCommands = new ListOfCommands();
        currentState = initialState;
        window = new Window(map, scale, this);
    }
    /**
     * Change the current state of the controller
     * @param state the new current state
     */
    protected void setCurrentState(State state){
        currentState = state;
    }

    // Methods corresponding to user events
    /**
     * Method called by window after a click on the button "Add a request"
     */
    public void addRequest() {currentState.addRequest(this, window);}
    /**
     * Method called by window after a click on the button "Add Courier"
     */
    public void addCourier(){currentState.addCourier(this,window);}
    /**
     * Method called by window after selecting a Courier with the radio button
     */
    public void chooseCourier(){currentState.chooseCourier(this,window,listOfCommands);}
    /**
     * Method called by window after a click on the button "Cancel"
     */
    public void cancel(){ currentState.cancel(this, window, map);}
    /**
     * Method called by window after a click on the button "Confirm"
     */
    public void confirm(){ currentState.confirm(this, window, map);}
    /**
     * Method called by window after a click on the button "Delete Request"
     */
    public void deleteRequest(){currentState.deleteRequest(this, window);}
    /**
     * Method called by window after a click on the button ""Modify Request""
     */
    public void modifyRequest(){currentState.modifyRequest(this, window);}
    /**
     * Method called by window after a click on the button "Load Map"
     */
    public void loadMap() {currentState.loadMap(this, map, window, listOfCommands);}
    /**
     * Method called by window after a click on the button "Save Requests"
     */
    public void saveRequests() {currentState.saveRequests(map);}
    /**
     * Method called by window after a click on the button "Load Requests"
     */
    public void loadRequests() {currentState.loadRequests(window, map, listOfCommands);}
    /**
     * Method called by window after selecting a TimeWindow with the radio button
     */
    public void chooseTimeWindow(){
        currentState.chooseTimeWindow(this, window, listOfCommands);
    }
    /**
     * Method called by window after left click
     */
    public void leftClick(Intersection i) {
        currentState.leftClick(this, window,map,listOfCommands,i);
    }
    /**
     * Method called by window after a click on the textual view of a Request
     */
    public void clickOnTextualRequest(Request r){currentState.clickOnTextualRequest(this, window, map, r, listOfCommands);}
    /**
     * Method called by window if a delivery point is unreachable
     */
    public void warn (String warningMessage){
        currentState.warn(this,window,warningMessage);
    }
    /**
     * Method called by window after a click on the button "Compute Tour"
     */
    public void computeTour(){ currentState.computeTour(this,window,map,listOfCommands);}
    /**
     * Method called by window after a click on the button "Generate Roadmap"
     */
    public void generateRoadmap() {
        currentState.generateRoadmap(this, window,map);
    }

    /**
     * Method called by window after a click on the button "Undo"
     */
    public void undo(){
        currentState.undo(listOfCommands);
    }

    /**
     * Method called by window after a click on the button "Redo"
     */
    public void redo(){
        currentState.redo(listOfCommands);
    }
}

