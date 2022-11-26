package tests;

import junit.framework.TestCase;
import models.Intersection;
import models.IntersectionFactory;
import org.junit.Before;
import org.junit.Test;
import xml.XMLOpener;

import java.io.File;

public class XMLOpenerTest {
    XMLOpener instance;

    @Before
    public void setup(){
        instance = XMLOpener.getInstance();
    }

    @Test
    public void getDescription() {
        String description = instance.getDescription();
        description.contains("XML");
    }

    @Test
    public void open() throws Exception {
        File file = instance.open(XMLOpener.MODES.OPEN);
    }

    @Test
    public void getInstance() {
        assert(XMLOpener.getInstance() != null);
    }
}