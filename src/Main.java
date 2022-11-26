import algorithms.Dijkstra;
import algorithms.TourComputer;
import controller.Controller;
import models.Itinerary;
import models.Map;
import models.Segment;

import javax.swing.*;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) {
        testMapView();
        //testTourCompute();
    }
    private static void testMapView (){
        models.Map map = new Map();
        new Controller(map,2);
    }

}
// TODO : REORGANISER CE MAIN

