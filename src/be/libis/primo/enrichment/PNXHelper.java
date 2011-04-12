/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.libis.primo.enrichment;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Mehmet Celik, LIBIS/K.U.Leuven
 * @version 0.1
 */
public class PNXHelper {

    private PNXHelper() {
    }

    public static synchronized NodeList xpath(Document xml, String x) {
        NodeList result = null;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            result = (NodeList) xpath.evaluate(x, xml, XPathConstants.NODESET);
        } catch (Exception ex) {
            Logger.getLogger(PNXHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static synchronized NodeList appendXml(Document xml, NodeList nodeList, String fragmentString) {
        NodeList result = null;

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Element element = db.parse(new ByteArrayInputStream(fragmentString.getBytes())).getDocumentElement();
            Node fragment = xml.importNode(element, true);

            nodeList.item(0).getParentNode().appendChild(fragment);
        } catch (Exception ex) {
            Logger.getLogger(PNXHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public static synchronized String xml2str(Document dcmnt) {
        String xmlString = "";

        try {
            Transformer transformer;
            transformer = TransformerFactory.newInstance().newTransformer();

            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(dcmnt);
            transformer.transform(source, result);
            xmlString = result.getWriter().toString();

        } catch (Exception ex) {
            Logger.getLogger(PNXHelper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return xmlString;
    }

    public static synchronized String getRecordId(Document xml) {
        String recordId = "";
        try {
            NodeList result = PNXHelper.xpath(xml, "//control/sourcerecordid");

            for (int i = 0; i < result.getLength(); i++) {
                recordId = result.item(i).getTextContent();
                /* Usefull when enriching dedupped records
                if (recordId.indexOf("$$V") > -1) {
                recordId = recordId.substring(recordId.indexOf("$$V")+3, recordId.indexOf("$$O"));
                }
                 */
            }
        } catch (Exception ex) {
            Logger.getLogger(PNXHelper.class.getName()).log(Level.INFO, null, ex);
            Logger.getLogger(PNXHelper.class.getName()).log(Level.INFO, "Error in getRecordId", ex.getMessage());
        }
        return String.format("%09d", Integer.parseInt(recordId));
    }

    public static synchronized String getSourceId(Document xml) {
        String sourceId = "";
        try {
            NodeList result = PNXHelper.xpath(xml, "//control/sourceid");

            for (int i = 0; i < result.getLength(); i++) {
                sourceId = result.item(i).getTextContent();
            }
        } catch (Exception ex) {
            Logger.getLogger(PNXHelper.class.getName()).log(Level.INFO, null, ex);
            Logger.getLogger(PNXHelper.class.getName()).log(Level.INFO, "Error in getSourceId", ex.getMessage());
        }
        return sourceId;
    }

    public static synchronized void logXML(Document xml, String prefix) {
        try {
            BufferedWriter rlog = null;
            String sDcmnt = "";
            String recordId = PNXHelper.getRecordId(xml);
            sDcmnt = PNXHelper.xml2str(xml);
            rlog = new BufferedWriter(new FileWriter("/tmp/" + prefix + recordId + ".xml", true));
            rlog.write(sDcmnt);
            rlog.flush();
            rlog.close();
        } catch (IOException ex) {
            Logger.getLogger(PNXHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
