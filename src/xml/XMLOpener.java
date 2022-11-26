package xml;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class XMLOpener extends FileFilter {
    @Override
    public boolean accept(File file) {
        return file != null;
    }

    @Override
    public String getDescription() {
        return "XML files";
    }

    public enum MODES {OPEN, SAVE}

    private static XMLOpener instance = null;
    private final String defaultDirectory = "data";

    /**
     * Singleton.
     * @return
     */
    public static XMLOpener getInstance() {
        if (instance == null) instance = new XMLOpener();
        return instance;
    }

    public File open(XMLOpener.MODES mode) throws Exception {
        JFileChooser jFileChooser = new JFileChooser(defaultDirectory);
        jFileChooser.setFileFilter(this);
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        switch (mode) {
            case OPEN -> jFileChooser.showOpenDialog(null);
            case SAVE -> jFileChooser.showSaveDialog(null);
        }

        File theChosenOne = jFileChooser.getSelectedFile();
        if (theChosenOne == null) throw new Exception("An error occurred when accessing the file");
        return theChosenOne;
    }
}
