/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring.xml;

import com.cision.us.monitoring.SolrMessage;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml3;

/**
 *
 * @author james.allenspach
 */
public class SolrXmlHandler extends DefaultHandler {
    
    Logger log = LoggerFactory.getLogger(SolrXmlHandler.class);
    
    ArrayList<SolrMessage> messages = new ArrayList<SolrMessage>();
    
    boolean inDocument = false;
    Integer results = 0;
    HashMap<String, String> itemInfo = new HashMap<String, String>();
    String currentKey = "", currentValue = "";
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
//        System.out.println(qName);
        
        if (qName.equals("result"))
            results = new Integer(attributes.getValue("numFound"));

        if (qName.equals("doc"))
            inDocument = true;
        
        if (inDocument && (qName.equals("str") || qName.equals("int") || qName.equals("date")))
            currentKey = attributes.getValue("name");
    }
    
    @Override
    public void characters(char[] ch, int start, int length)  {
        if (inDocument && ! currentKey.equals(""))
            currentValue += escapeHtml3(String.copyValueOf(ch, start, length));
    }
    
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
//        System.out.println(qName);
        if (qName.equals("doc")) {
            inDocument = false;
            
            log.debug("context = {}, facility = {}, from = {}, hostname = {}, id = {}, program = {}, severity = {}, tag = {}",
                    new Object[] {itemInfo.get("context"), itemInfo.get("facility"), itemInfo.get("from"),
                    itemInfo.get("hostname"), itemInfo.get("id"), itemInfo.get("program"),
                    itemInfo.get("severity"), itemInfo.get("tag")});
            try {
                SolrMessage thisone = new SolrMessage();
                thisone.setContext(itemInfo.get("context"));
                thisone.setFacility(itemInfo.get("facility"));
                thisone.setFrom(itemInfo.get("from"));
                thisone.setHostname(itemInfo.get("hostname"));
                thisone.setId(itemInfo.get("id"));
                thisone.setMsg(itemInfo.get("msg"));
                thisone.setProgram(itemInfo.get("program"));
                thisone.setSeverity(itemInfo.get("severity"));
                thisone.setTag(itemInfo.get("tag"));

                thisone.setPriority(new Integer(itemInfo.get("priority")));

                String tmp = itemInfo.get("timestamp");
                tmp = tmp.replace("Z", "-0500");
//                System.out.println("*** TMP = " + tmp);
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"); // "2012-06-23T09:50:02-0000"
                thisone.setTimestamp(f.parse(tmp));
                
                messages.add(thisone);
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            itemInfo.clear();
        } else if (inDocument) {
//            System.out.println(currentKey + " => " + currentValue);
            itemInfo.put(currentKey, currentValue);
            currentKey = "";
            currentValue = "";
        }
    }
    
    public void endDocument() throws SAXException {
        log.debug("total number of messages = {}", messages.size());
    }
    
    public ArrayList<SolrMessage> messages() { return this.messages; }
    public Integer resultcount() { return this.results; }
}
