package tests;

import models.Map;
import org.junit.Before;
import org.junit.Test;
import xml.XMLDeserializer;

public class XMLDeserializerTest {

    private XMLDeserializer deserializer;

    @Before
    public void setup() {
        deserializer = new XMLDeserializer();
    }

    @Test
    public void deserializeMap() throws Exception {
        Map map = new Map();

        deserializer.open();
        deserializer.deserialize(map);

        assert map.getListIds().size() > 0;
        assert map.getWarehouse() != null;

        Long id = map.getListIds().get(0);
        assert map.getEdges(id).size() > 0;
    }

}