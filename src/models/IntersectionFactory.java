package models;

import java.util.HashMap;

public class IntersectionFactory {
    private static HashMap<Long, Intersection> intersections;
    private static double latMax,latMin,lonMax,lonMin;
    /**
     * Create a factory of intersections, use of FlyWeight design pattern
     * based on intersection id.
     */
    public static void initFactory() {
        IntersectionFactory.intersections = new HashMap<>();
        latMin = 500.0;
        lonMin = 500.0;
        latMax = 0;
        lonMax = 0;
    }

    public static double getLatMax() {
        return latMax;
    }

    public static double getLatMin() {
        return latMin;
    }
    public static double getLonMax(){
        return lonMax;
    }

    public static double getLonMin() {
        return lonMin;
    }

    public static Intersection createIntersection(Long id) {
        return IntersectionFactory.intersections.get(id);
    }

    public static Intersection createIntersection(Long id, double longitude, double latitude) {
        IntersectionFactory.intersections.put(id, new Intersection(id, longitude, latitude));
        return IntersectionFactory.intersections.get(id);
    }
    public static void getBoards (){
        for (Long key:intersections.keySet()){
            Intersection i = intersections.get(key);
            if (latMin>i.getLatitude())latMin=i.getLatitude();
            if (lonMin>i.getLongitude())lonMin=i.getLongitude();
            if (lonMax<i.getLongitude())lonMax=i.getLongitude();
            if(latMax<i.getLatitude())latMax=i.getLatitude();
        }
    }
    public static HashMap<Long, Intersection> getIntersection() {
        return IntersectionFactory.intersections;
    }

    public static void clear() {
        intersections.clear();
        latMin = 500.0;
        lonMin = 500.0;
        latMax = 0;
        lonMax = 0;
    }
}
