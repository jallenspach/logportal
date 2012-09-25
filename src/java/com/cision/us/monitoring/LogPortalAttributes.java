/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring;

import java.util.List;
import com.cision.us.monitoring.service.SolrData;


/**
 *
 * @author james.allenspach
 */
public class LogPortalAttributes {
    List<String> programs = null;
    
    public LogPortalAttributes() {
        SolrData sd = new SolrData();
        programs = sd.getPrograms();
    }
}
