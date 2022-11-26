package xml;

import models.*;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedList;

public class GenerateRoadMap {
    private static LinkedList<Tour> tours = new LinkedList<>();


    public GenerateRoadMap(LinkedList tours) {
        this.tours = tours;

    }

    public void generate() {
        JFileChooser fc = new JFileChooser("data");
        fc.showSaveDialog(null);
        File f = fc.getSelectedFile();
        if (f == null) {
            System.out.println("No file selected!");
            return;
        }

        double lon, lat;

        try {
            FileWriter fileWriter = new FileWriter(f);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.println();
            printWriter.println("\tDate: " + new Date());
            printWriter.println("=============================Road map for all computed tours===========================");
            if(tours.isEmpty()){
                System.err.println("no tours to export");
                return;
            }
            for (Tour tour : tours) {
                int numberOfDeliveries = tour.getOrderedRequests().size();

                if (tour.getOrderedItineraries().isEmpty()) {

                    printWriter.println("\tCourier: " + tour.getCourier().getName());
                    printWriter.println("\tNo requests");
                    printWriter.close();

                } else {
                    printWriter.println();
                    printWriter.println("\tCourier: " + tour.getCourier().getName());
                    printWriter.println("\t***\t08:00 warehouse:");
                    for (int i = 0; i < numberOfDeliveries + 1; i++) {
                        Itinerary itinerary = tour.getOrderedItineraries().get(i);

                        int h;
                        LinkedList<Segment> lseg = itinerary.getSegments();

                        for (int j = 0; j < lseg.size(); j += h) {
                            String segment_name = lseg.get(j).getName();
                            double segment_length = 0;
                            for (h = j; h < lseg.size(); h++) {
                                Segment current = lseg.get(h);
                                String current_name = current.getName();

                                if (!current_name.equals(segment_name)) break;
                                segment_length += current.getLength();

                            }
                            printWriter.println("\t\t\t" + segment_name + "(" +
                                    Math.floor(segment_length / 10) / 100 + " Km)");
                        }
                        printWriter.println();
                        if (i < numberOfDeliveries) {
                            printWriter.print("\t***\t" + tour.getOrderedRequests().get(i).getDeliveredTime() + " request " + (i + 1) + ":");
                            lon = tour.getOrderedRequests().get(i).getDestination().getLongitude();
                            lat = tour.getOrderedRequests().get(i).getDestination().getLatitude();
                            printWriter.println("\t\t(latitude: " + lat + " longitude: " + lon + ")");
                            printWriter.println("\t\t\twaiting duration:" + tour.getOrderedRequests().get(i).getWaitingTime() + " minutes");


                        }
                    }
                    printWriter.println("\t***\twarehouse");
                    printWriter.println();

                }
            }
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void test() {
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
        Tour tourToadd = new Tour(itineraries, reqs);
        Courier courier = new Courier(0);
        courier.setTour(tourToadd);
        courier.setName("Malek");
        tourToadd.setCourier(courier);
        tours.add(tourToadd);
        System.out.println(tours);
        GenerateRoadMap generateRoadMap = new GenerateRoadMap(tours);


        generateRoadMap.generate();

    }

    public static void main(String[] args) {
        test();
    }

}
