package models;

public class TimeWindow {
    private Integer start;
    private Integer end;

    /**
     * Create a TimeWindow
     * @param start
     * @param end
     */
    public TimeWindow(Integer start, Integer end) {
        this.start = start;
        this.end = end;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }
    @Override
    public String toString() {
        return "time window [" + start + " - " + end + "]\n";
    }
}
