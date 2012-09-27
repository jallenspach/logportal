/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring.service;

import com.cision.us.monitoring.xml.SolrFacetHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author james.allenspach
 */
public class SolrData {
    
    public List<String> getHostnames() {
        return new ArrayList<String> (Arrays.asList("amapp1","c1services1","cmfeed1","cmwebservice1","d3-rpt1","d3-web1","d3-web2","essapp1","essapp2","fvapp1","fvapp2","labsapp1","mbgappsrvr","mdwapp1","mdwapp2","mdwsolr1","mqapp1","planapi1",
"planapi2","plansolr1","plansolr2","plansolr3","plansolr4","purldb1","purldb2","purlweb1","purlweb2","rdssvc1","rdssvc2","rdsweb1","rdsweb2","solrindx1","solrindx2","ssapp1","ssapp2","ssapp3",
"ssengine1","ssindx1","ssindx10","ssindx11","ssindx12","ssindx13","ssindx2","ssindx3","ssindx4","ssindx5","ssindx6","ssindx7","ssindx8","ssindx9","utilappweb1"));
    }
    
    public List<String> getLevels() {
        ArrayList<String> returnValue = new ArrayList<String>(Arrays.asList("DEBUG", "INFO", "NOTICE", "WARNING", "ERR"));
        return returnValue;
    }
    
    public List<String> getPrograms() {
        String solrUrl = "http://logstore1.qwestcolo.local:8080/solr/select/?q=*%3A*&version=2.2&start=0&rows=0&indent=on&facet=true&facet.field=program";
        //List<String> returnValue = (List<String>) new ArrayList();
        ArrayList<String> returnValue = new ArrayList<String>(Arrays.asList("abrtd","acpid","anacron","auditd","bonobo-activation-server","console-kit-daemon","crond","crontab","dhclient","init","kernel","local4","mswineventlog","networkmanager","ntpd","oddjobd","polkitd(authority=local)","postfix","restorecond","rhnsd","rpc.idmapd","rpcbind","rsyslogd","sendmail","smbd","snmpd","sshd","su","sudo","unix_chkpwd","xinetd"));

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        
        Collections.sort(returnValue);
        return returnValue;
    }
}
