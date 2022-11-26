package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import controller.Controller;

public class ButtonListener implements ActionListener {

    private final Controller controller;

    public ButtonListener(Controller controller){
        this.controller = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Method called by the button listener each time a button is clicked
        // Forward the corresponding message to the controller
        //
        for (String c : Window.listCouriers){
            if(e.getActionCommand()==c) controller.chooseCourier();
        }
        for (int i =0;i<Window.rbtnTexts.length;i++){
            if(e.getActionCommand()==Window.rbtnTexts[i]) controller.chooseTimeWindow();
        }
        switch (e.getActionCommand()) {
                case Window.LOAD_MAP -> controller.loadMap();
                case Window.ADD_REQUEST -> controller.addRequest();
                case Window.CANCEL -> controller.cancel();
                case Window.SAVE_REQUESTS -> controller.saveRequests();
                case Window.LOAD_REQUESTS-> controller.loadRequests();
                case Window.ADD_COURIER -> controller.addCourier();
                case Window.DELETE_REQUEST -> controller.deleteRequest();
                case Window.MODIFY_REQUEST -> controller.modifyRequest();
                case Window.CONFIRM -> controller.confirm();
                case Window.UNDO -> controller.undo();
                case Window.REDO -> controller.redo();
                case Window.COMPUTE_TOUR -> controller.computeTour();
                case Window.ROADMAP -> controller.generateRoadmap();
            }
    }
}