package controller;

import algorithms.TourComputer;
import models.*;
import view.Window;
import xml.GenerateRoadMap;
import xml.XMLDeserializer;
import xml.XMLSerializer;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

public class LoadedMapState implements State {

    @Override
    public void addRequest(Controller c, Window w) {
        w.enableButtons("Add Request");
        w.changeBtnLabelAndColor("Add Request", "Cancel", Color.red);
        w.switchRButtonsAddRequest();
        w.displayMessage("Add a request: Choose a specific Time-Window using the <br/> Radio Buttons.<br/> Click on [Cancel] to cancel this operation.");
        c.setCurrentState(c.addRequestState1);
    }

    @Override
    public void deleteRequest(Controller c, Window w) {
        w.enableButtons("Delete Request");
        w.changeBtnLabelAndColor("Delete Request", "Cancel", Color.red);
        w.displayMessage("Delete a request: [Left Click] to select a delivery point on the map or the textual view. <br/> Click on [Cancel] to cancel this operation.");
        c.setCurrentState(c.deleteRequestState);
    }

    @Override
    public void modifyRequest(Controller c, Window w) {
        w.enableButtons("Modify Request");
        w.changeBtnLabelAndColor("Modify Request", "Cancel", Color.red);
        w.displayMessage("Modify a request: [Left Click] on the map or the textual view to select the delivery point you wish to modify. <br/> Click on [Cancel] to cancel this operation.");
        c.setCurrentState(c.modifyRequestState1);
    }

    @Override
    public void loadMap(Controller c, Map m, Window w, ListOfCommands listOfCommands) {
        try {
            m.clear();
            m.fillMap();
        } catch (Exception e) {
            w.enableButtons("Load Map");
            c.setCurrentState(c.initialState);
        }
        listOfCommands.reset();
    }

    @Override
    public void computeTour(Controller c, Window w, Map m,ListOfCommands listOfCommands){
        w.displayMessage("The tour is being calculated, the program will be terminated in at most 20 sec");
        for (int id : m.getCouriers().keySet()){
            TourComputer tc = new TourComputer(m,id);

            tc.encodingStopsId();
            Long stopId = tc.createReducedGraph();
            if (stopId == 0) {
                Tour t = tc.getBestTour();
                if (m.getCouriers().get(id).hasTour()) {
                    Tour oldTour = m.getCouriers().get(id).getTour();
                    t.setCourier(m.getCouriers().get(id));
                    m.getCouriers().get(id).setName("courier number" + id);
                    m.getCouriers().get(id).setTour(t);
                    m.updateTour(oldTour, t);
                } else {
                    t.setCourier(m.getCouriers().get(id));
                    m.getCouriers().get(id).setName("courier number" + id);
                    m.getCouriers().get(id).setTour(t);
                    m.addTour(t);
                }

                w.displayRoadmapDialogue(t.toString());
            } else {
                Intersection i = m.getlistIntersections().get(stopId);
                w.displayMessage(i + "is not reachable. Please modify this request and choose another destination.");
            }
        }
        w.displayMessage("");
        w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request","Undo", "Redo","Export Roadmap");
        listOfCommands.reset();

    }


    @Override
    public void generateRoadmap(Controller c, Window w, Map m) {
        LinkedList<Tour> tours = m.getTours();
        GenerateRoadMap gr = new GenerateRoadMap(tours);
        gr.generate();
        w.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request", "Undo", "Redo", "Export Roadmap");
    }


    @Override
    public void undo(ListOfCommands listOfCdes) {
        listOfCdes.undo();
    }

    @Override
    public void redo(ListOfCommands listOfCdes) {
        listOfCdes.redo();
    }

    public void loadRequests(Window window, Map map, ListOfCommands listOfCommands) {
        LinkedList<Request> requests = new LinkedList<>();
        try {
            XMLDeserializer deserializer = new XMLDeserializer();
            deserializer.open();
            deserializer.deserialize(requests);

            for (Request request : requests) { listOfCommands.add(new AddCommand(map, request,null)); }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred when loading the requests, please check if the file you chose is correct.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        window.enableButtons("Load Map", "Save Requests", "Load Requests", "Add Request", "Delete Request", "Modify Request", "Undo", "Redo", "Compute Tour");
    }

    @Override
    public void saveRequests(Map map) {
        try {
            XMLSerializer serializer = new XMLSerializer();
            LinkedList<Request> allRequests = new LinkedList<>();
            for (Courier c : map.getCouriers().values()) {
                allRequests.addAll(c.getRequests());
            }
            serializer.save(allRequests);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred when saving the requests, please check if the file you chose is correct.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
