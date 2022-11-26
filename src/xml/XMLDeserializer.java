package xml;

import models.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedList;

public class XMLDeserializer {

    private Document document;
    public XMLDeserializer() {};

    /**
     * Open a JFileChooser Window for the user to choose an
     * XML file to deserialize.
     * @throws Exception, an error arose when opening the file.
     */
    public void open() throws Exception {
        XMLOpener xmlOpener = XMLOpener.getInstance();
        File file = xmlOpener.open(XMLOpener.MODES.OPEN);
        DocumentBuilder docBuilder = DocumentBuilderFactory
                .newInstance().newDocumentBuilder();
        document = docBuilder.parse(file);
    }

    /**
     * Deserialize a given Map object from the opened XML
     * document.
     * @param map object to be deserialized.
     * @return Map object that has been deserialized.
     */
    public Map deserialize(Map map) {
        map.clear(); // Clean existing object
        Element root = document.getDocumentElement();

        // Create all intersections
        NodeList intersectionElements = root.getElementsByTagName("intersection");
        for (int i = 0; i < intersectionElements.getLength(); i++) {
            // Storage is handled by factory
            map.addIntersection(createIntersection((Element) intersectionElements.item(i)));
        }

        // Create all segments
        NodeList segmentsElements = root.getElementsByTagName("segment");
        for (int i = 0; i < segmentsElements.getLength(); i++) {
            Element segmentElement = (Element) segmentsElements.item(i);
            Segment segment = createSegment(segmentElement);
            map.addEdge(Long.parseLong(segmentElement.getAttribute("origin")), segment);
        }

        // Locate warehouse
        Element element = (Element) root.getElementsByTagName("warehouse").item(0);
        map.setWarehouse(IntersectionFactory.createIntersection(Long.parseLong(element.getAttribute("address"))));

        // Update the maximums and minimums
        IntersectionFactory.getBoards();
        return map;
    }

    /**
     * Deserialize a list of Request objects from
     * the loaded XML document.
     * @param requests empty list that filled on deserialization.
     * @return The list of Request deserialized.
     */
    public LinkedList<Request> deserialize(LinkedList<Request> requests) throws Exception {
        Element root = document.getDocumentElement();
        requests.clear();

        // Create all requests
        NodeList elements = root.getElementsByTagName("request");
        if (elements.getLength() < 1) throw new Exception();
        for (int i = 0; i < elements.getLength(); i++) {
            Request request = createRequest((Element) elements.item(i));
            if (request.getDestination() == null) continue; // Is not contained in current map
            requests.add(request);
        }

        return requests;
    }

    /**
     * Deserialize a Request object from a given Element.
     * @param item Element to deserialize.
     * @return the deserialized Request object.
     */
    private static Request createRequest(Element item) {
        // Deserialize TimeWindow
        Integer start = Integer.parseInt(item.getAttribute("start"));
        Integer end = Integer.parseInt(item.getAttribute("end"));
        TimeWindow timeWindow = new TimeWindow(start, end);

        // Deserialize Intersection
        Long id = Long.parseLong(item.getAttribute("location"));
        Intersection intersection = IntersectionFactory.createIntersection(id);

        return new Request(timeWindow, intersection);
    }

    /**
     * Deserialize a Segment object from a given Element.
     * @param item Element to deserialize.
     * @return the deserialized Segment object.
     */
    private static Segment createSegment(Element item) {

        // Deserialize attributes
        Long idOrigin = Long.parseLong(item.getAttribute("origin"));
        Long idDestination = Long.parseLong(item.getAttribute("destination"));
        String name = item.getAttribute("name");
        Double length = Double.parseDouble(item.getAttribute("length"));

        return new Segment(
            IntersectionFactory.createIntersection(idOrigin),
            IntersectionFactory.createIntersection(idDestination),
            length, name
        );
    }

    /**
     * Deserialize an Intersection object from a given Element.
     * @param item Element to deserialize.
     * @return the deserialized Intersection object.
     */
    private static Intersection createIntersection(Element item) {

        // Deserialize attribute
        double latitude = Double.parseDouble(item.getAttribute("latitude"));
        double longitude = Double.parseDouble(item.getAttribute("longitude"));
        long id = Long.parseLong(item.getAttribute("id"));

        return IntersectionFactory.createIntersection(id, longitude, latitude);
    }

}



