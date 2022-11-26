package xml;

import models.Request;
import models.TimeWindow;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;
import java.util.LinkedList;

public class XMLSerializer {
    Document document;

    public XMLSerializer() {}

    /**
     * Save a serialized list of request objects into a XML file chosen
     * via a JFileChooser.
     * @param requests list of requests to save.
     * @throws Exception an error occurred during saving.
     */
    public void save(LinkedList<Request> requests) throws Exception {
        File xml = XMLOpener.getInstance().open(XMLOpener.MODES.SAVE);
        StreamResult result = new StreamResult(xml);

        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        DOMSource source = new DOMSource(serialize(requests));

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }

    /**
     * Serialize a list of Request objects to XML document.
     * @param requests list of requests to serialize.
     * @return Serialized result.
     */
    public Element serialize(LinkedList<Request> requests) {
        Element root = document.createElement("requests");
        for (Request request : requests) {
            Element element = document.createElement("request");
            createAttribute(element, "location", Long.toString(request.getDestination().getId()));
            createAttribute(element, "start", Long.toString(request.getTimeWindow().getStart()));
            createAttribute(element, "end", Long.toString(request.getTimeWindow().getEnd()));
            root.appendChild(element);
        }

        return root;
    }

    /**
     * Create a new XML attribute to a given Element.
     * @param root Element on which the attribute is added
     * @param name Name of the attribute
     * @param value Value of the attribute
     */
    private void createAttribute(Element root, String name, String value) {
        Attr attribute = document.createAttribute(name);
        root.setAttributeNode(attribute);
        attribute.setValue(value);
    }
}
