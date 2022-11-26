package models;

import observer.Observable;

import java.util.LinkedList;

public class Itinerary extends Observable {
    private LinkedList<Segment> segments;
    private Intersection start;
    private Intersection destination;
    private double length;
    private TimeStamp departureTime;
    private TimeStamp arrivalTime;
    private final double SPEED = 15/3.6;

    public Itinerary() {

    }
    /**
     * Create an itinerary
     * @param segments the list of segments of the itinerary
     * @param start the starting intersection
     * @param destination the destination intersection
     */
    public Itinerary(LinkedList<Segment> segments, Intersection start, Intersection destination) {
        this.segments = segments;
        this.start = start;
        this.destination = destination;
        updateLength();

    }

    private void updateLength() {
        length = 0;
        for (Segment segment : segments) {
            length += segment.getLength();
        }
    }

    public LinkedList<Segment> getSegments() {
        return segments;
    }

    public void setSegments(LinkedList<Segment> segments)
	{
		this.segments = segments;
		updateLength();
	}

    public double getLength() {
        return length;
    }

    public String toString(){
        String result = "The courier starts at the point : " + start + " at "+ departureTime;
        result += ", then stops at the point : " + destination + " at "+arrivalTime+ " to make a delivery.\n";
        return result;
    }

    public TimeStamp getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(TimeStamp arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setDepartureTime(TimeStamp departureTime) {
        this.departureTime = departureTime;
    }
    public Integer getDeplacementTimeInMinutes(){
        return (int)(length/(SPEED*60));
    }
    public Intersection getDestination() {
        return destination;
    }

    public Intersection getStart() {
        return start;
    }
}
