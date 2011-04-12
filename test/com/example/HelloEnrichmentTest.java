/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import be.libis.primo.enrichment.PNXHelper;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Mehmet Celik, LIBIS/K.U.Leuven
 */
public class HelloEnrichmentTest {
    public Document xml = null;

    public HelloEnrichmentTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
       try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            this.xml = db.parse("./Resources/pnxRecord.xml");
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(HelloEnrichmentTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(HelloEnrichmentTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(HelloEnrichmentTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testEnrich() {
        System.out.println("enrich");        
        Map map = null;
        HelloEnrichment instance = new HelloEnrichment();
        int expResult = 1;
        Document result = instance.enrich(xml, map);
        System.out.println(PNXHelper.xml2str(result));

        NodeList searchNodeList = PNXHelper.xpath(result, "//search/description");
        
        assertEquals(1, searchNodeList.getLength());
        assertEquals("Hello World", searchNodeList.item(0).getTextContent());
    }

}