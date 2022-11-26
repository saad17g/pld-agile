package view;

import controller.Controller;
import models.Request;
import models.Tour;
import models.Visitor;
import observer.Observable;
import observer.Observer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextualRequest extends JLabel{
    private String text;
    private Request request;
    private Controller controller;
    public TextualRequest(Request request, Controller controller){
        this.controller = controller;
        this.request = request;

        this.text = "<html><ul><li>Delivery to " + request.getDestination() +" in " + request.getTimeWindow();

        if(request.getDeliveredTime()!=null){
            text+= "delivered at " + request.getDeliveredTime()+"</li></ul></html>";
            if(request.getDeliveredTime().getHour()>=request.getTimeWindow().getEnd())
                this.setForeground(Color.red);
        }

        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                System.out.println("Request " + request + "has been selected");
                controller.clickOnTextualRequest(request);
            }
        });
    }

    @Override
    public String getText(){
        return text;
    }
}