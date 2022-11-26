package view;

import controller.Controller;
import models.Intersection;
import models.IntersectionFactory;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

	private final Controller controller;
	private final MapView mapView;
	private final Window window;

	public MouseListener(Controller c, MapView mv, Window w){
		this.controller = c;
		this.mapView = mv;
		this.window = w;
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		// Method called by the mouse listener each time the mouse is clicked 
		switch (evt.getButton()) {
			case MouseEvent.BUTTON1 -> {
				Intersection i = coordinates(evt);
				if (i != null) controller.leftClick(i);
				else controller.warn("No delivery point found. Please click on the map.");
			}
			default -> {
			}
		}
	}

	/*public void mouseMoved(MouseEvent evt) {
		// Method called by the mouse listener each time the mouse is moved
		Intersection p = coordinates(evt);
		if (p != null)
			controller.mouseMoved(p); 
	}*/
	
	private Intersection coordinates(MouseEvent evt){
		MouseEvent e = SwingUtilities.convertMouseEvent(window, evt, mapView);
		int eX = e.getX();
		int eY = e.getY();
		if (eX>mapView.getWidth()|| eX <0 || eY> mapView.getHeight()||eY<0){
			//the chosen delivery point is not on the map
			return null;
		} else {
			return mapView.convertPixelToIntersection(e.getX(), e.getY());
		}
	}
}
