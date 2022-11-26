package models;

public class Segment {
    private final Intersection start;
    private final Intersection end;
    private final double length;
    private final String name;

    /**
     * Create a Segment
     * @param start
     * @param end
     * @param length
     * @param name
     */
    public Segment(Intersection start, Intersection end, double length, String name) {
        this.start = start;
        this.end = end;
        this.length = length;
        this.name = name;
    }

    public double getLength() {
        return this.length;
    }

    public Intersection getStart() {
        return start;
    }

    public Intersection getEnd() {
        return end;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "start=" + start +
                ", end=" + end +
                ", length=" + length +
                ", name='" + name + '\'' +
                '}';
    }
}