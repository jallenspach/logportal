<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%-- 
    Document   : request
    Created on : Jun 29, 2012, 2:44:03 PM
    Author     : james.allenspach
--%>
<jsp:useBean id="solrReq" scope="page" class="com.cision.us.monitoring.service.SolrRequest" />

<jsp:setProperty name="solrReq" property="*" />
    

<%@page contentType="text/xml" pageEncoding="UTF-8"%>
<body>
    <%= solrReq.getResults() %>
</body>