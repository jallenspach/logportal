/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring.service;

import com.cision.us.monitoring.SolrMessage;
import com.cision.us.monitoring.xml.SolrXmlHandler;
import java.util.ArrayList;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.conn.HttpHostConnectException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author james.allenspach
 */
public class SolrRequest {
    
    Logger log = LoggerFactory.getLogger(SolrRequest.class);
    
    String solrUrl = "";
    String programTxt = "", severityTxt = "", hostnameTxt = "", msgTxt = "", generatedTxt = "", sortbyTxt = "";
    Integer startRow = 0;
    
    
    public void setSolrUrl(String surl) {
        this.solrUrl = surl;
    }
    public String getSolrUrl() { return this.solrUrl; }
    
    public void setProgramTxt(String ptxt) {
        this.programTxt = ptxt;
    }
    public String getProgramTxt() { return this.programTxt; }
    
    public void setSeverityTxt(String stxt) {
        this.severityTxt = stxt;
    }
    public String getSeverityTxt() { return this.severityTxt; }
    
    public void setHostnameTxt(String htxt) {
        this.hostnameTxt = htxt;
    }
    public String getHostnameTxt() { return this.hostnameTxt; }
    
    public void setMsgTxt(String mtxt) {
        this.msgTxt = mtxt;
    }
    public String getMsgTxt() { return this.msgTxt; }
    
    public void setGeneratedTxt(String gtxt) {
        this.generatedTxt = gtxt;
    }
    public String getGeneratedTxt() { return this.generatedTxt; }

    public void setSortbyTxt(String stxt) {
        this.sortbyTxt = stxt;
    }
    public String getSortbyTxt() { return this.sortbyTxt; }
    
    public void setStartRow(Integer sr) { 
        this.startRow = sr;
    }
    public Integer getStartRow() { return this.startRow; }

    
    String xml_escape(String s) {
        String tmp = s.replaceAll("&", "&amp;");
        tmp = tmp.replaceAll("<", "&lt;");
        tmp = tmp.replaceAll(">", "&gt;");
        return tmp;
    }
    
    public String getResults() {
        
        String url = this.solrUrl; // "http://logstore1.qwestcolo.local:8080/solr/coreSyslog/select/?";
        String queryString = "version=2.2&start=" + startRow + "&rows=100&indent=on&q=";
        ArrayList<String> qstrings = new ArrayList<String>();
        if (! programTxt.equals(""))
             qstrings.add(programTxt);
        if (! severityTxt.equals(""))
            qstrings.add(severityTxt);
        if (! hostnameTxt.equals(""))
            qstrings.add(hostnameTxt);
        if (! msgTxt.equals(""))
            qstrings.add(msgTxt);
        if (! generatedTxt.equals(""))
            qstrings.add(generatedTxt);
        for (int n = 0; n < qstrings.size(); ++n) {
            if (n > 0)
                queryString += "+AND+";
            queryString += qstrings.get(n);
        }
        url += queryString;
        
        if (! this.sortbyTxt.equals(""))
            url += "&sort=" + this.sortbyTxt;
        
        log.debug("solr url {}", url);
        
        String results = "";
        
        try {
            
            Content c = Request.Get(url).connectTimeout(15000).execute().returnContent();
            log.debug("Content: {}", c.asString());
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser parser = spf.newSAXParser();
            SolrXmlHandler handler = new SolrXmlHandler();
            parser.parse(c.asStream(), handler);
            
            for (SolrMessage m : handler.messages()) {
                results += "<item>" + 
                        "<context>" + m.getContext() + "</context>" + 
                        "<facility>" + m.getFacility() + "</facility>" + 
                        "<from>" + m.getFrom() + "</from>" + 
                        "<hostname>" + m.getHostname() + "</hostname>" +
                        "<id>" + m.getId() + "</id>" + 
                        "<program>" + m.getProgram() + "</program>" + 
                        "<severity>" + m.getSeverity() + "</severity>" + 
                        "<tag>" + m.getTag() + "</tag>" + 
                        "<msg>" + xml_escape(m.getMsg()) + "</msg>" + 
                        "<timestamp>" + m.getTimestamp().toString() + "</timestamp>" + 
                        "</item>";
            }
            
/*            URLConnection conn = new URL(url).openConnection();
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            InputStream response = conn.getInputStream();*/
            
            
            results = "<max>" + handler.resultcount().toString() + "</max><items>" + results + "</items>";
            
        } catch (HttpHostConnectException e) {
            results = "<error>Can't connect to Solr server</error>";
        } catch (IllegalArgumentException e) {
            results = "<error>Invalid syntax in search</error>";
        } catch (Exception e) {
            log.error("Exception when getting Solr data", e);
            results = "<error>General error</error>";
        }
        log.debug("return xml {}", results);
        return results;
    }
}
