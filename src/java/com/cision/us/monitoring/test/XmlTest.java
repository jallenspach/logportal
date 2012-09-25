/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring.test;

import com.cision.us.monitoring.SolrMessage;
import com.cision.us.monitoring.xml.SolrXmlHandler;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 *
 * @author james.allenspach
 */
public class XmlTest {
    static String xml = "  <doc>" + 
    "<str name=\"context\">default</str>" + 
    "<str name=\"facility\">local4</str>" + 
    "<str name=\"from\">10.12.218.82</str>" + 
    "<str name=\"generated\">2012-07-16T15:16:59Z</str>" + 
    "<str name=\"hostname\">ssindx4</str>" + 
    "<str name=\"id\">7049ffa9-ac85-45aa-9ea0-f2e868fd8763</str>" + 
    "<str name=\"msg\">2012-07-16 15:16:59,492 [solr-exec-15] INFO  org.apache.solr.core.SolrCore-  [core1dayC] webapp=/solr path=/select params={facet=on&amp;version=2&amp;fl=id,score&amp;facet.field=feed_name&amp;fsv=true&amp;wt=javabin&amp;rows=0&amp;start=0&amp;q=indexed_date_rounded:[NOW/DAY+TO+NOW]&amp;f.feed_name.facet.limit=160&amp;isShard=true} hits=372143 status=0 QTime=31 </str>" + 
    "<int name=\"priority\">6</int>" + 
    "<str name=\"program\">local4</str>" + 
    "<str name=\"severity\">info</str>" + 
    "<str name=\"tag\">local4:</str>" + 
    "<date name=\"timestamp\">2012-07-16T15:16:59Z</date>" + 
  "</doc>";

    
    public static void main(String[] args) {
        String results = "";
        try {
            
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            SolrXmlHandler handler = new SolrXmlHandler();
            InputStream is = new ByteArrayInputStream(xml.getBytes());
            parser.parse(is, handler);
            
            for (SolrMessage m : handler.messages()) {
                System.out.println("<item>" + 
                        "<context>" + m.getContext() + "</context>" + 
                        "<facility>" + m.getFacility() + "</facility>" + 
                        "<from>" + m.getFrom() + "</from>" + 
                        "<hostname>" + m.getHostname() + "</hostname>" +
                        "<id>" + m.getId() + "</id>" + 
                        "<program>" + m.getProgram() + "</program>" + 
                        "<severity>" + m.getSeverity() + "</severity>" + 
                        "<tag>" + m.getTag() + "</tag>" + 
                        "<msg>" + m.getMsg() + "</msg>" + 
                        "<timestamp>" + m.getTimestamp().toString() + "</timestamp>" + 
                        "</item>");
            }
            
            results = "<max>" + handler.resultcount().toString() + "</max><items>" + results + "</items>";
            
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
