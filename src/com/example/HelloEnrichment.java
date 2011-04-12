/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.example;

import be.libis.primo.enrichment.PNXHelper;
import com.exlibris.primo.api.enrichment.plugin.EnrichmentPlugIn;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 *
 * @author Mehmet Celik, LIBIS/K.U.Leuven
 */
public class HelloEnrichment implements EnrichmentPlugIn {

    @Override
    public Document enrich(Document dcmnt, Map map) {
        NodeList searchNodeList = PNXHelper.xpath(dcmnt, "//search/*");
        PNXHelper.appendXml(dcmnt, searchNodeList, "<description>Hello World</description>");
        return dcmnt;
    }

}
