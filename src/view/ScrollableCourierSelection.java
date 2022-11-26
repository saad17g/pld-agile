package view;

import javax.swing.*;

public class ScrollableCourierSelection extends JScrollPane {
    public ScrollableCourierSelection(Box courierPanel, Window w) {
        super(courierPanel);
        w.getContentPane().add(this);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }
}
