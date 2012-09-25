/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cision.us.monitoring;

import java.util.Date;

/**
 *
 * @author james.allenspach
 */
public class SolrMessage {
    String context, facility, from, hostname, id, msg, program, severity,tag;
    Integer priority;
    Date timestamp;
    
    public void setContext(String c) { this.context = c; }
    public String getContext() { return this.context; }

    public void setFacility(String c) { this.facility = c; }
    public String getFacility() { return this.facility; }

    public void setFrom(String c) { this.from = c; }
    public String getFrom() { return this.from; }

    public void setHostname(String c) { this.hostname = c; }
    public String getHostname() { return this.hostname; }

    public void setId(String c) { this.id = c; }
    public String getId() { return this.id; }

    public void setMsg(String c) { this.msg = c; }
    public String getMsg() { return this.msg; }

    public void setProgram(String c) { this.program = c; }
    public String getProgram() { return this.program; }

    public void setSeverity(String c) { this.severity = c; }
    public String getSeverity() { return this.severity; }

    public void setTag(String c) { this.tag = c; }
    public String getTag() { return this.tag; }

    public void setPriority(Integer c) { this.priority = c; }
    public Integer getPriority() { return this.priority; }

    public void setTimestamp(Date c) { this.timestamp = c; }
    public Date getTimestamp() { return this.timestamp; }
}

