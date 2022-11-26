package models;

import models.Request;

public interface Visitor {
    // Implement design pattern Visitor to display a request and a tour
    public void display(Request r);
    public void display (Tour t);
    public void display (Courier c);

}
