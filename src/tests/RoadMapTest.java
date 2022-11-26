package tests;

import models.*;
import org.junit.Test;
import xml.GenerateRoadMap;

import java.util.LinkedList;

public class RoadMapTest {
    @Test
    public void test() {
        IntersectionFactory.initFactory();

        Intersection intersection1 = IntersectionFactory.createIntersection(25303831L, 45.74979, 4.87572);
        Intersection intersection2 = IntersectionFactory.createIntersection(25321433L, 45.74969, 4.873468);
        Intersection intersection3 = IntersectionFactory.createIntersection(25321422L, 45.749027, 4.873145);
        Intersection intersection4 = IntersectionFactory.createIntersection(1288817934L, 45.749992, 4.8702283);
        Intersection intersection5 = IntersectionFactory.createIntersection(1259561375L, 45.75071, 4.870559);
        Intersection intersection6 = IntersectionFactory.createIntersection(1288817934L, 45.749992, 4.8702283);
        Intersection intersectionDes = IntersectionFactory.createIntersection(25321456L, 45.749214, 4.875591);
        Request request1 = new Request(new TimeWindow(8, 9), intersection2);

        Request request2 = new Request(new TimeWindow(9, 10), intersection5);

        LinkedList<Request> reqs = new LinkedList<>();
        TimeStamp date1 = new TimeStamp(8, 5);
        TimeStamp date2 = new TimeStamp(9, 10);

        request1.setDeliveryTime(date1);
        request2.setDeliveryTime(date2);

        reqs.add(request1);
        reqs.add(request2);

        Segment segment11 = new Segment(intersection1, intersectionDes, 64.89446, "Rue Feuillat");
        Segment segment12 = new Segment(intersection2, intersectionDes, 174.66516, "Rue Fiol");
        LinkedList<Segment> segments1 = new LinkedList<>();
        segments1.add(segment11);
        segments1.add(segment12);
        Itinerary itinerary = new Itinerary(segments1, intersection1, intersectionDes);
        Segment segment21 = new Segment(intersection2, intersection3, 77.777405, "Rue du Docteur Rebatel");
        Segment segment22 = new Segment(intersection3, intersection4, 250.5025, "Rue de Montbrillant");
        Segment segment23 = new Segment(intersection4, intersection5, 83.691925, "Rue Professeur Louis Roche");
        LinkedList<Segment> segments2 = new LinkedList<>();
        segments2.add(segment21);
        segments2.add(segment22);
        segments2.add(segment23);
        Itinerary itinerary2 = new Itinerary(segments2, intersection2, intersection5);
        Segment segment31 = new Segment(intersection5, intersection6, 83.691925, "Rue Professeur Louis Roche");
        Segment segment32 = new Segment(intersection2, intersection3, 77.777405, "Rue du Docteur Rebatel");
        Segment segment33 = new Segment(intersectionDes, intersection2, 174.66516, "Rue Fiol");
        LinkedList<Segment> segments3 = new LinkedList<>();
        segments3.add(segment31);
        segments3.add(segment32);
        segments3.add(segment33);
        Itinerary itinerary3 = new Itinerary(segments3, intersection5, intersection2);
        LinkedList<Itinerary> itineraries = new LinkedList<>();
        itineraries.add(itinerary);
        itineraries.add(itinerary2);
        itineraries.add(itinerary3);

        LinkedList<Tour> tours = new LinkedList<>();
        Tour tour = new Tour(itineraries, reqs);
        Courier courier = new Courier(0);
        courier.setTour(tour);
        courier.setName("MALEK");
        tour.setCourier(new Courier(0));
        tours.add(tour);
        GenerateRoadMap generateRoadMap = new GenerateRoadMap(tours);
        generateRoadMap.generate();

    }
}
