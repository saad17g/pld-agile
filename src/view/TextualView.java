package view;

import controller.Controller;
import models.*;
import observer.Observable;
import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

public class TextualView extends JPanel implements Observer, Visitor {
    private final Map map;
    private ArrayList<TextualRequest> textualRequests;
    private Controller controller;
    private Window window;
    public TextualView(Map map, Window window, Controller controller) {
        super();

        this.textualRequests = new ArrayList<>();
        this.controller = controller;
        this.map = map;
        this.window = window;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("List of requests:"));

        window.getContentPane().add(this);
        map.addObserver(this);
    }

    @Override
    public void update(Graphics g) {
        super.update(g);
        System.out.println("Graphic update called!");
    }

    @Override
    public void update(Observable o, Object arg) {
        removeAll();

        Iterator<Courier> couriers = map.getCouriers().values().iterator();
        for (Iterator<Courier> it = couriers; it.hasNext(); ) {
            Courier courier  = it.next();
            String txt = courier.toString();
            JLabel courierLabel = new JLabel(txt);
            this.add(courierLabel);
            if(courier.getTour() !=null) {
                LinkedList<Request> rqs = courier.getTour().getOrderedRequests();
                for (Request rq : rqs) {
                    TextualRequest textualRequest = new TextualRequest(rq, controller);
                    this.add(textualRequest);
                    this.revalidate();
                }
            } else {
                LinkedList<Request> rqs = courier.getRequests();
                for (Request rq : rqs) {
                    TextualRequest textualRequest = new TextualRequest(rq, controller);
                    this.add(textualRequest);
                    this.revalidate();
                }
            }
        }

        repaint();
    }
    @Override
    public void display(Request r) {
        TextualRequest textReq = new TextualRequest(r, controller);
        textualRequests.add(textReq);
        textReq.setLocation(0, 10);
        this.add(textReq);
    }

    @Override
    public void display(Tour t) {

    }

    @Override
    public void display(Courier c) {

    }
}