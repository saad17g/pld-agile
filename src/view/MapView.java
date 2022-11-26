package view;

import java.awt.*;

import models.Map;


import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import models.*;

import observer.Observable;
import observer.Observer;

public class MapView extends JPanel implements Observer, Visitor {
    private static final long serialVersionUID = 1L;
    private static final int IMAGE_SIZE = 25;
    private static final int MARKER_SIZE = 8;
    private static final int LINE_THICKNESS = 3;
    private int scale;
    private int viewHeight;
    private int viewWidth;
    private Map map;
    private Graphics g;
    private final BufferedImage warehouse;
    private int colorIndex;
    private static final Color[] palette = new Color[]
            {new Color(39,82,95),new Color(90,167,166),new Color(159,182,119),new Color(243,196,48),new Color(74,200,241)};
    /**
     * Create the map view with scale s in window w
     * @param map the map
     * @param s the scale
     */
    public MapView(Map map, int s, Window w)  {
        super();
        map.addObserver(this); // this observes map
        this.scale = s;
        viewHeight = 300*s;
        viewWidth = 300*s;
        setLayout(null);
        setBackground(Color.white);
        setSize(viewWidth, viewHeight);
        w.getContentPane().add(this);
        this.map = map;
        colorIndex = 0;
        try {
            this.warehouse= ImageIO.read(new File("src/assets/warehouse.png"));
        } catch (IOException e) {
            throw new RuntimeException("Error when loading warehouse image",e);
        }
    }
    /**
     * Method called each this must be redrawn
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g1 = (Graphics2D)g;
        g.setColor(Color.gray);
        g.drawRect(0, 0, viewWidth, viewHeight);
        this.g = g;
        LinkedList<Long> listIds = map.getListIds();
        //Drawing all segments
        if (listIds.size()>0){
            for (Long id : listIds){
                ArrayList<Segment> segments = map.getEdges(id);
                for (Segment s: segments){
                    Pixel pStart = convertIntersectionToPixel(s.getStart());
                    Pixel pEnd = convertIntersectionToPixel(s.getEnd());
                    /*System.out.println(pStart);
                    System.out.println(pEnd);*/
                    paintSegment(g1,pStart,pEnd,Color.black);
                }
            }
        }
        //Drawing all requests
        Iterator<Courier> itC = map.getCouriers().values().iterator();
        while(itC.hasNext()) {
            Courier c = itC.next();
            Iterator<Request> it = c.getRequests().iterator();
            while (it.hasNext()) it.next().display(this); //display requests
        }
        //Drawing all tours
        Iterator<Tour> itT = map.getTours().iterator();
        while(itT.hasNext()){
            itT.next().display(this);
        }
        //Drawing the warehouse
        Intersection warehouse = map.getWarehouse();
        if(warehouse!=null){
            drawWarehouse(convertIntersectionToPixel(warehouse));
        }
    }

    public int getScale(){
        return scale;
    }

    public void setScale(int e) {
        viewWidth = (viewWidth/scale)*e;
        viewHeight = (viewHeight/scale)*e;
        setSize(viewWidth, viewHeight);
        scale = e;
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public int getViewWidth() {
        return viewWidth;
    }

    /**
     * Method called by objects observed by this each time they are modified
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg != null){ // arg is a request that has been added to the map
            if(arg instanceof Request) {
                Request f = (Request) arg;
                f.addObserver(this);
            }
            else if (arg instanceof Tour){
                Tour t = (Tour) arg;
                t.addObserver(this);
            }
            /*else {
                Intersection i = (Intersection) arg;
                i.addObserver(this);  // this observes i
            }*/
        }
        repaint();
    }

    /**
     * update the MapView with given map and display it
     * @param givenMap
     */
    public void displayMap (Map givenMap){
        this.map = givenMap;
        this.revalidate();
        this.repaint();
    }

    /**
     * transform latitude/longitude point to a pixel (x,y) on mercator projection
     * @param lat latitude
     * @param lon longitude
     * @return a pixel (x,y)
     */

    public Pixel latLonToPixels (double lat, double lon){
        int x  = (int)Math.floor((lon-map.getLonMin())/(map.getLonMax()-map.getLonMin())*viewHeight);
        int y  = (int)Math.floor((1-(lat-map.getLatMin())/(map.getLatMax()-map.getLatMin()))*viewWidth);
        return new Pixel(x,y);
    }

    /**
     * find the closet intersection to a pixel
     * @param x
     * @param y
     * @return an intersection
     */
    public Intersection convertPixelToIntersection(int x, int y){
        double lat = (1-(double)y/viewWidth)*(map.getLatMax()-map.getLatMin())+map.getLatMin();
        double lon = ((double)x/viewHeight)*(map.getLonMax()-map.getLonMin())+map.getLonMin();
        double dis = Long.MAX_VALUE;
        Intersection i = null;
        LinkedList<Long> ids = map.getListIds();
        for( Long id : ids){
            ArrayList<Segment> segments = map.getEdges(id);
            for(Segment s : segments){
                double dist1 = calculateDistance(s.getStart(), lat, lon);
                double dist2 = calculateDistance(s.getEnd(), lat, lon);

                if(dist1<dis){
                    dis = dist1;
                    i = s.getStart();
                }
                if(dist2<dis){
                    dis = dist2;
                    i = s.getEnd();
                }
            }
        }
        return i;
    }

    private static double calculateDistance(Intersection i1, double lat, double lon){
        return Math.sqrt((Math.pow(i1.getLatitude()-lat,2)+Math.pow(i1.getLongitude()-lon,2)));
    }
    /**
     * transform an intersection to a pixel
     * @param i an intersection
     * @return
     */
    public Pixel convertIntersectionToPixel (Intersection i){
        return latLonToPixels(i.getLatitude(),i.getLongitude());
    }

    public void paintSegment (Graphics2D g,Pixel p1, Pixel p2, Color c){
        g.setColor(c);
        int p1X = p1.getX();
        int p1Y = p1.getY();
        int p2X = p2.getX();
        int p2Y = p2.getY();
        Line2D.Float line = new Line2D.Float(p1X,p1Y,p2X,p2Y);
        g.draw(line);
    }
    public void drawWarehouse (Pixel p){
        g.drawImage(warehouse, p.getX()-IMAGE_SIZE/2, p.getY()-IMAGE_SIZE/2,IMAGE_SIZE,IMAGE_SIZE, this);
    }
    public void drawRoute(Graphics2D g2d, Pixel p1, Pixel p2, Color c){
        int p1X = p1.getX();
        int p1Y = p1.getY();
        int p2X = p2.getX();
        int p2Y = p2.getY();
        g2d.setColor(c);
        g2d.setStroke(new BasicStroke(LINE_THICKNESS));
        Line2D.Float line = new Line2D.Float(p1X,p1Y,p2X,p2Y);
        g2d.draw(line);
    }

    @Override
    public void display(Request r) {
        Pixel p = convertIntersectionToPixel(r.getDestination());
        g.setColor(Color.red);
        g.fillOval(p.getX()-MARKER_SIZE/2, p.getY()-MARKER_SIZE/2,MARKER_SIZE,MARKER_SIZE);
    }
    @Override
    public void display (Tour t){
        colorIndex = colorIndex%5;
        Graphics2D g1 = (Graphics2D)g;
        LinkedList<Itinerary> itineraries = t.getOrderedItineraries();
        for (Itinerary i:itineraries){
            LinkedList<Segment>segments = i.getSegments();
            for (Segment s:segments) {
                drawRoute(g1, convertIntersectionToPixel(s.getStart()), convertIntersectionToPixel(s.getEnd()),palette[colorIndex]);
            }
        }
        colorIndex++;
    }
    @Override
    public void display (Courier c){}

}
