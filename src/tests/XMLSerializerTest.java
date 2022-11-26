package tests;

import models.Map;
import org.junit.Before;
import org.junit.Test;
import xml.XMLDeserializer;
import xml.XMLSerializer;

public class XMLSerializerTest {

    private XMLSerializer serializer;

    @Before
    public void setup() {
        serializer = new XMLSerializer();
    }

    // TODO: Implement unittests, low priority as not a real use case.
}