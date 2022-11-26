package tests;


import algorithms.Dijkstra;
import algorithms.TourComputer;
import models.Map;
import models.*;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertTrue;

public class TourTest {

    private static Map map ;
    private static Dijkstra d;

    private static final int idCourier =0;
    @Before
    public void Initialisation() throws Exception {
        map = new Map();
        long t1 = currentTimeMillis();
        map.fillMap();
        long t2 = currentTimeMillis();
        System.err.println("Large Map loading : " + (t2 - t1) + "ms");

        Random       random    = new Random();
        List<Long> keys      = new ArrayList<>(map.getlistIntersections().keySet());
        Long       randomKey = keys.get( random.nextInt(keys.size()) );
        Intersection start = map.getlistIntersections().get(randomKey);
        d = new Dijkstra(map, start);
    }
    @Test
    public void testGetItinerary() {

        Intersection iDest;
        Random randomR1 = new Random();
        List<Long> keysR1 = new ArrayList<>(map.getlistIntersections().keySet());
        Long randomKeyR1 = keysR1.get(randomR1.nextInt(keysR1.size()));
        iDest = map.getlistIntersections().get(randomKeyR1);

        boolean boolTest = true;

        Itinerary itineraryTest = d.getItinerary(iDest.getId());

        if (itineraryTest != null) {
            List<Segment> segments = itineraryTest.getSegments();
            System.out.println(segments.toString());
            Segment currSeg = segments.get(0);
            Segment nextSeg = segments.get(1);

            if (segments.get(segments.size() - 1).getEnd() != iDest) boolTest = false;
            else {
                for (Segment s : segments) {
                    if (currSeg.getEnd() != nextSeg.getStart()) {
                        boolTest = false;
                        break;
                    }
                    currSeg = nextSeg;
                    nextSeg = segments.get(segments.indexOf(s) + 2);
                }
            }
        }
        assertTrue("testGetItinerary failed", boolTest);
    }
    @Test
    public void tourMap() {

        HashMap<Long, Intersection> listIntersections = map.getlistIntersections();

        LinkedList<Request> listRequests = new LinkedList<>();

        Intersection i1 = listIntersections.get(25321469L);
        Intersection i2 = listIntersections.get(26576965L);

        Request req1 = new Request(new TimeWindow(8, 9), i1);
        Request req2 = new Request(new TimeWindow(9, 10), i2);
        listRequests.add(req1);
        listRequests.add(req2);

        map.addDeliveryPoint(req1);
        map.addDeliveryPoint(req2);

        long t1 = currentTimeMillis();

        TourComputer tc = new TourComputer(map, idCourier);
        tc.encodingStopsId();
        tc.createReducedGraph();
        Tour tour = tc.getBestTour();

        long t2 = currentTimeMillis();
        System.err.println("calculation time : " + (t2 - t1) + "ms");

        List<Itinerary> itineraries = tour.getOrderedItineraries();
        System.out.println(itineraries);
        Itinerary it1 = itineraries.get(1);
        List<Segment> lSeg = it1.getSegments();
        System.out.println(lSeg);
        Segment fstSegment = lSeg.get(0);
        assert (fstSegment.getName().equals("Rue Feuillat"));
        Segment lstSegment = lSeg.get(lSeg.size() - 1);
        assert (lstSegment.getName().equals("Boulevard Vivier-Merle"));
    }

    @Test
    public void deleteRequestLargeMap() throws Exception {
        models.Map map = new Map();
        map.fillMap();
        //System.out.println(map);
        System.out.println("map loaded");
        Request r1 = new Request(new TimeWindow(8, 9), new Intersection(25321469L, 45.748405, 4.875445));
        Request r2 = new Request(new TimeWindow(8, 9), new Intersection(26576965L, 45.758797, 4.8583546));
        map.addDeliveryPoint(r1);
        map.addDeliveryPoint(r2);
        map.addDeliveryPoint(new Request(new TimeWindow(8, 9), new Intersection(3932053909L, 4.8617926, 45.771446)));
        TourComputer tc = new TourComputer(map,idCourier);
        tc.encodingStopsId();
        tc.createReducedGraph();
        Tour tour = tc.getBestTour();
        System.out.println("warehouse " + map.getWarehouse().getId());
        System.out.println("ordered itineraries : \n" + tour.getOrderedItineraries());
        assert (tour.getOrderedItineraries().size() == 4);

        tour = tc.deleteRequest(tour, r1);
        System.out.println("*****************after deletion of request************");
        System.out.println("itineraries : \n" + tour.getOrderedItineraries());
        assert (tour.getOrderedItineraries().size() == 3);

        tour = tc.addRequest(tour, r1, r2);
        System.out.println("*****************after adding request************");
        System.out.println("itineraries : \n" + tour.getOrderedItineraries());
        assert (tour.getOrderedItineraries().size() == 4);
        System.out.println(tour.getOrderedItineraries().size());


    }


}
