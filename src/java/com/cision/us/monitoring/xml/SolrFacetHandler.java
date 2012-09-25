/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring.xml;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author james.allenspach
 */
public class SolrFacetHandler extends DefaultHandler {
    int facetFields = 0;
    String facetName = "";
    ArrayList<String> facets = new ArrayList<String>();
    
    public SolrFacetHandler(String facetName) {
        this.facetName = facetName;
    }
    
    public ArrayList<String> facets() {
        return this.facets;
    }
    
    public void startElement (String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals("lst") && attributes.getValue("name").equals(facetName))
            facetFields = 1;
        if (qName.equals("int") && facetFields == 1) {
            System.out.println("name = " + attributes.getValue("name"));
            facets.add(attributes.getValue("name"));
        }
    }
    
    public void endElement (String uri, String localName, String qName) {
        if (facetFields == 1 && qName.equals("lst"))
            facetFields = 0;
    }
    
    public void characters (char ch[], int start, int length) {
        
    }
}
