package view;

import javax.swing.*;

public class ScrollableTextualView extends JScrollPane {
    public ScrollableTextualView(TextualView textualView, Window w) {
        super(textualView,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        w.getContentPane().add(this);
    }
}