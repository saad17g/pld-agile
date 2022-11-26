package models;
import observer.Observable;

public class Intersection extends Observable {
    private final double longitude;
    private final double latitude;
    private final long id;

    /**
     * Create an intersection of coordinates(longitude, latitude).
     * @param id
     * @param longitude
     * @param latitude
     */
    public Intersection(long id, double longitude, double latitude) {
        // Constructor protected, using IntersectionFactory to create new objects
        // (FlyWeight design pattern).
        this.longitude = longitude;
        this.latitude = latitude;
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "(" + latitude +", "+longitude+")";
    }
}

