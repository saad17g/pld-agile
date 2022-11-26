package view;

import controller.Controller;
import models.Map;
import models.Request;
import models.TimeWindow;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Objects;

public class Window extends JFrame {
    protected static final String LOAD_MAP = "Load Map";
    protected static final String SAVE_REQUESTS ="Save Requests";
    protected static final String LOAD_REQUESTS = "Load Requests";
    protected static final String ADD_REQUEST = "Add Request";
    protected static final String ADD_COURIER = "Add Courier";
    protected static final String DELETE_REQUEST = "Delete Request";
    protected static final String MODIFY_REQUEST ="Modify Request";
    protected static final String UNDO = "Undo";
    protected static final String REDO = "Redo";
    protected static final String COMPUTE_TOUR ="Compute Tour";
    protected static final String ROADMAP = "Export Roadmap";
    protected static final String CANCEL ="Cancel";
    protected static final String CONFIRM ="Confirm";
    protected static final int NUMBER_TIME_WINDOW =4;
    private ArrayList<JButton> buttons;
    private ArrayList<JRadioButton> rButtonsAddRequest;
    private ArrayList<JRadioButton> rButtonsModifyRequest;
    private ArrayList<JRadioButton> rButtonsAddCourier;
    private ButtonGroup bgAddRequest;
    private ButtonGroup bgModifyRequest;
    private ButtonGroup bgAddCourier;
    protected static  LinkedList<String> listCouriers;
    private final JLabel messageFrame;
    private final MapView mapView;
    private ButtonListener buttonListener;
    private ScrollableCourierSelection scrollableCourierSelection;
    private final TextualView textualView;
    private final Box courierPanel;
    private final ScrollableTextualView scrollableTextualView;
    protected final String[] buttonTexts = new String[]{LOAD_MAP, SAVE_REQUESTS, LOAD_REQUESTS, ADD_REQUEST,ADD_COURIER, MODIFY_REQUEST,DELETE_REQUEST,UNDO, REDO, COMPUTE_TOUR,ROADMAP};
    protected static final String[] rbtnTexts = new String[]{"8-9","9-10", "10-11", "11-12"};
    private final int buttonHeight = 40;
    private final int buttonWidth = 150;
    private final int radioButtonHeight = buttonHeight/2 - 5;
    private final int radioButtonWidth = buttonWidth/2 - 5;
    private final int messageFrameHeight = 150;
    private final int textualViewWidth = 500;
    public ArrayList<JRadioButton> getrButtonsAddRequest() {
        return rButtonsAddRequest;
    }
    public ArrayList<JRadioButton> getrButtonsModifyRequest() {return rButtonsModifyRequest;}
    public MapView getMapView() {
        return mapView;
    }
    public Window(Map map, int s, Controller controller){
        listCouriers = new LinkedList<>();
        courierPanel = Box.createVerticalBox();
        setLayout(null);
        createButtons(controller);
        messageFrame = new JLabel();
        messageFrame.setBorder(BorderFactory.createTitledBorder("Messages..."));
        getContentPane().add(messageFrame);
        mapView = new MapView(map,s,this);
        textualView = new TextualView(map, this, controller);
        scrollableTextualView = new ScrollableTextualView(textualView,this);
        MouseListener mouseListener = new MouseListener(controller, mapView, this);
        addMouseListener(mouseListener);
        addMouseMotionListener(mouseListener);
        setWindowSize();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        setTitle("LeafRaison");
    }
    public void addCourierButtons(){
        String newButton = "Courier " + listCouriers.size();
        listCouriers.add(newButton);
        JRadioButton newRBtCourier = new JRadioButton(newButton);
        newRBtCourier.addActionListener(buttonListener);
        bgAddCourier.add(newRBtCourier);
        rButtonsAddCourier.add(newRBtCourier);
        courierPanel.add(newRBtCourier);
        revalidate();
    }

    private void createButtons(Controller controller) {
        buttonListener = new ButtonListener(controller);
        buttons = new ArrayList<JButton>();
        rButtonsAddRequest = new ArrayList<JRadioButton>();
        rButtonsModifyRequest = new ArrayList<JRadioButton>();
        rButtonsAddCourier = new ArrayList<JRadioButton>();
        bgAddRequest = new ButtonGroup();
        bgModifyRequest = new ButtonGroup();
        bgAddCourier = new ButtonGroup();
        //Create buttons to choose time window when adding a request
        for(int i = 0; i<buttonTexts.length; i++)
        {
            JButton btn = new JButton(buttonTexts[i]);
            int x= 10, y = 0;
            if( i<=3)
                y = 10 + (5+buttonHeight)*i;
            else if(i==4)
                y = 10 + (5+buttonHeight)*(i+1);
            else if(i==5)
                y = 10+ (5+buttonHeight)*(i+3);
            else
                y = 10+ (5+buttonHeight)*(i+4);
            btn.setLocation(x, y);
            buttons.add(btn);
            btn.setSize(buttonWidth, buttonHeight);
            btn.setFocusable(false);
            btn.setFocusPainted(false);
            btn.addActionListener(buttonListener);
            getContentPane().add(btn);
        }
        //Create buttons to choose time window when modifying a request
        for(int i = 0; i<rbtnTexts.length; i++)
        {
            JRadioButton rbtn = new JRadioButton(rbtnTexts[i]); rbtn.setEnabled(false);
            JRadioButton rbtnBis = new JRadioButton(rbtnTexts[i]); rbtnBis.setVisible(false);
            rButtonsAddRequest.add(rbtn);
            rButtonsModifyRequest.add(rbtnBis);
            int x=0, y=0;
            if(i==0 || i==1) y = 4*buttonHeight+30;
            if(i==2 || i==3) y = 4*buttonHeight+50;
            if(i==0 || i ==2) x=10;
            if(i==1 || i==3) x=100;
            rbtn.setBounds(x, y, radioButtonWidth, radioButtonHeight);
            rbtnBis.setBounds(x, 25+y+5*buttonHeight,radioButtonWidth, radioButtonHeight );
            rbtn.addActionListener(buttonListener);
            rbtnBis.addActionListener(buttonListener);
            bgAddRequest.add(rbtn);
            bgModifyRequest.add(rbtnBis);
            getContentPane().add(rbtn);
            getContentPane().add(rbtnBis);
        }
        //Create buttons to choose courier when adding/modifying a request
        for(String c: listCouriers){
            JRadioButton rbtCourier = new JRadioButton(c);
            rbtCourier.setVisible(false);
            rbtCourier.addActionListener(buttonListener);
            bgAddCourier.add(rbtCourier);
            rButtonsAddCourier.add(rbtCourier);
            courierPanel.add(rbtCourier);
        }
        courierPanel.setBackground(Color.LIGHT_GRAY);
        courierPanel.setBorder(BorderFactory.createTitledBorder("Choose a courier"));
        courierPanel.setBounds(10,280,buttonWidth,buttonHeight*2);
        getContentPane().add(courierPanel);
        scrollableCourierSelection= new ScrollableCourierSelection(courierPanel,this);
        scrollableCourierSelection.setBounds(10,280,buttonWidth,buttonHeight*2);
        enableButtons(LOAD_MAP);
    }
    /**
     * Define the size of the frame and its subframes wrt the size of the view
     */
    private void setWindowSize() {
        int allButtonHeight = buttonHeight*buttonTexts.length;
        int windowHeight = Math.max(mapView.getViewHeight(),allButtonHeight)+messageFrameHeight;
        int windowWidth = mapView.getViewWidth()+buttonWidth+textualViewWidth+10;
        setSize(windowWidth, windowHeight);
        messageFrame.setSize(textualViewWidth,messageFrameHeight);
        messageFrame.setLocation(20+mapView.getViewWidth()+buttonWidth,mapView.getViewHeight()-messageFrameHeight);
        mapView.setLocation(buttonWidth+20, 0);
        textualView.setSize(textualViewWidth,mapView.getViewHeight()-messageFrameHeight);
        textualView.setLocation(20+mapView.getViewWidth()+buttonWidth,0);
        scrollableTextualView.setSize(textualView.getSize());
        scrollableTextualView.setLocation(textualView.getLocation());
    }
    /**
     * Display m in the message zone
     * @param m the message to display
     */
    public void displayMessage(String m) {
        messageFrame.setText("<html>"+m+"</html>");
    }
    /**
     * Change button label
     * @param from the initial label
     * @param to the new label
     * @param c the new color
     */

    public void changeBtnLabelAndColor(String from, String to, Color ...c){
        for(JButton btn:buttons){
            if(Objects.equals(btn.getText(), from)){
                btn.setText(to);
                if(c.length>0)btn.setBackground(c[0]);
            }
        }
    }

    /**
     * reinitialize radio buttons after choosing a time window
     */
    public void deselectRButtons(String type) {
        if(type.equals("add"))bgAddRequest.clearSelection();
        if(type.equals("modify"))bgModifyRequest.clearSelection();
        if (type.equals("courier"))bgAddCourier.clearSelection();

    }
    public void switchRButtonsAddRequest(){
        for(JRadioButton rbtn: rButtonsAddRequest)
        {
            rbtn.setEnabled(!rbtn.isEnabled());
        }
    }
    public void changeVisibilityRButtonsModifyRequest(){
        for(JRadioButton rbtn:rButtonsModifyRequest){
            rbtn.setVisible(!rbtn.isVisible());
        }
    }
    public void changeVisibilityRButtonsAddCourier(){
        for(JRadioButton rbtn:rButtonsAddCourier){
            rbtn.setVisible(!rbtn.isVisible());
        }
    }
    public TimeWindow getSelectedRButton(String type){
        String timeWindowStr="";
        if(type.equals("add")){
            for(JRadioButton rbtn:rButtonsAddRequest){
                if(rbtn.isSelected())timeWindowStr=rbtn.getText();
            }
        }else if(type.equals("modify")) {
            for(JRadioButton rbtn:rButtonsModifyRequest){
                if(rbtn.isSelected())timeWindowStr=rbtn.getText();
            }
        }
        int separ = timeWindowStr.indexOf("-");
        Integer start = Integer.parseInt(timeWindowStr.substring(0,separ));
        Integer end = Integer.parseInt(timeWindowStr.substring(separ+1));
        return new TimeWindow(start, end);
    }
    public int getSelectedCourier(){
        String result="";
        for(JRadioButton rbtn:rButtonsAddCourier){
            if(rbtn.isSelected())result=rbtn.getText();
        }
        return Integer.parseInt(result.substring(8));
    }

    public void enableButtons(String... btns){
        for(JButton btn:buttons)
            btn.setEnabled(false);
        for(JButton btn:buttons){
            for(String s:btns){
                if(btn.getText().equals(s)) {
                    btn.setEnabled(true);
                }
            }
        }
    }
    public void displayRoadmapDialogue(String dialogue){
        JOptionPane.showMessageDialog(this,dialogue,"Information",JOptionPane.INFORMATION_MESSAGE);
    }
}