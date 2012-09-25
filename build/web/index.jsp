<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="java.util.Calendar" %>
<%@page import="java.text.SimpleDateFormat" %>
<%-- 
    Document   : index
    Created on : Jun 20, 2012, 8:59:45 AM
    Author     : james.allenspach
--%>
<jsp:useBean id="solrData" scope="page" class="com.cision.us.monitoring.service.SolrData" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Log Search Utility </title>
        <link type="text/css" href="css/redmond/jquery-ui-1.8.21.custom.css" rel="Stylesheet" />	
        <script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
        <script type="text/javascript" src="js/jquery-ui-1.8.21.custom.min.js"></script>

        <script>

            function recalc_programs() {
                var ifr = document.getElementById('myfr').contentWindow;
                ifr.pgmValue = "";
                var e = document.forms["mainform"].elements;
                for (var i = 0; i < e.length; ++i) {
                    if (e[i].name == 'app' && e[i].checked == 1) {
                        if (ifr.pgmValue != '')
                            ifr.pgmValue = ifr.pgmValue + '+OR+';
                        ifr.pgmValue = ifr.pgmValue +  escape(e[i].value);
                    }
                }
                if (ifr.pgmValue != '')
                    ifr.pgmValue = 'program:(' + ifr.pgmValue + ')';
            }
            
            function recalc_level() {
                var ifr = document.getElementById('myfr').contentWindow;
                ifr.lvlValue = "";
                var e = document.forms["mainform"].elements;
                for (var i = 0; i < e.length; ++i) {
                    if (e[i].name == 'lvl' && e[i].checked == 1) {
                        if (ifr.lvlValue != '')
                            ifr.lvlValue = ifr.lvlValue + '+OR+';
                        ifr.lvlValue = ifr.lvlValue +  escape(e[i].value);
                    }
                }
                if (ifr.lvlValue != '')
                    ifr.lvlValue = "severity:(" + ifr.lvlValue + ')';
            }
            
            function recalc_hostname() {
                var ifr = document.getElementById('myfr').contentWindow;
                ifr.hnValue = "";
                var e = document.forms["mainform"].elements;
                for (var i = 0; i < e.length; ++i) {
                    if (e[i].name == 'hostname' && e[i].checked == 1) {
                        if (ifr.hnValue != '')
                            ifr.hnValue = ifr.hnValue + '+OR+';
                        ifr.hnValue = ifr.hnValue +  escape(e[i].value);
                    }
                }
                if (ifr.hnValue != '')
                    ifr.hnValue = "hostname:(" + ifr.hnValue + ')';
            }
            
            function recalc_txt() {
                var ifr = document.getElementById('myfr').contentWindow;
                ifr.txtValue = "";
                var str = document.forms[0].textSearch.value;
                ifr.txtValue_raw = str;
		
		var rx1 = /^\s*\"([^"]+)\"/;
		var rx2 = /^\s*(\S+)/;
		
		while (str != '') {
			if (rx1.test(str)) {
				var tmp = rx1.exec(str);
				if (ifr.txtValue != '')
					ifr.txtValue = ifr.txtValue + '+AND+';
				ifr.txtValue = ifr.txtValue + 'msg:%22' + escape(tmp[1]) + '%22';
				str = RegExp.rightContext;
			} else if (rx2.test(str)) {
				var tmp = rx2.exec(str);
				if (ifr.txtValue != '')
					ifr.txtValue = ifr.txtValue + '+AND+';
				ifr.txtValue = ifr.txtValue + 'msg:%22' + escape(tmp[1]) + '%22';
				str = RegExp.rightContext;
			} else {
				alert('cannot match ' + str);
				return false;
			}
		}
                if (ifr.txtValue != '')
                    ifr.txtValue = '(' + ifr.txtValue + ')';
            }
            
            function recalc_date() {
                var ifr = document.getElementById('myfr').contentWindow;
                ifr.dateValue = 'generated:[' + document.forms[0].textFrom.value+'T00:00:00Z+TO+' + document.forms[0].textTo.value+'T23:59:59Z]';
            }
            
            function generate_solr_url() {
                var ifr = document.getElementById('myfr').contentWindow;
                ifr.startRow = 0;
                recalc_date();
                recalc_level();
                recalc_programs();
                recalc_txt();
                ifr.solrUrl = "http://mprhdp1-dev.qwestcolo.local:8080/solr/coreSyslog/select/?";
//                ifr.solrUrl = document.forms['mainform'].solrUrl.options[document.forms['mainform'].solrUrl.selectedIndex].value;
                ifr.generate_solr_url();
            }

            $(document).ready(function() {
		$('#textFrom').datepicker({ dateFormat: 'yy-mm-dd' });
		$('#textTo').datepicker({ dateFormat: 'yy-mm-dd' });
		
		$('#accordion').accordion({autoHeight: false,navigation: true, collapsible: true });
		
		$("#resizable").resizable();

		$("#resizable").resizable({
			resize: function(event, ui) {
				$("#myfr").css({ "height": ui.size.height,"width":ui.size.width});
			}
		});
		
		$("#saveButton").button({ icons: { primary: "ui-icon-disk" }, text: false });
                $("#updateButton").button({text: false});
                $("#updateButton").click(function() { generate_solr_url(); return false; }); // prevent submit
		$("#updateButton").submit(function() { return false; }); // prevent submit
	});

        </script>

    </head>

    <body>
        <form id="mainform">
            <table style="border: 3px solid black">
                <tr>
                    <td> <h1> logging search utility </h1> </td>
                    <td style="width: 300px"> &nbsp; </td>
                </tr>
                <tr>
                    <td valign="top" style="verticalAlign: top">
<%--                        <select name="solrUrl">
                            <option> http://logstore1.qwestcolo.local:8080/solr/coreSyslog/select/? </option> 
                            <option> http://mprhdp1-dev.qwestcolo.local:8080/solr/coreSyslog/select/? </option> 
                            <option> http://mprhdp2-dev.qwestcolo.local:8080/solr/coreSyslog/select/? </option> 
                            <option> http://mprhdp3-dev.qwestcolo.local:8080/solr/coreSyslog/select/? </option> 
                        </select> --%>
                        
                        <div>
                            <%
                                Calendar today = Calendar.getInstance();
                                String today_str = String.format("%04d-%02d-%02d",
                                        today.get(Calendar.YEAR),
                                        today.get(Calendar.MONTH) + 1,
                                        today.get(Calendar.DAY_OF_MONTH)
                                        );
                                today.add(Calendar.HOUR, -24);
                                String yesterday_str = String.format("%04d-%02d-%02d",
                                        today.get(Calendar.YEAR),
                                        today.get(Calendar.MONTH) + 1,
                                        today.get(Calendar.DAY_OF_MONTH)
                                        );
                            %>
                            <input type="text" name="textSearch" id="textSearch" onClick="if (document.getElementById('myfr').contentWindow.txtValue_raw == '') { this.value = document.getElementById('myfr').contentWindow.txtValue_raw }" value="" onChange="recalc_txt()" />
				<input type="text" name="textFrom" id="textFrom" value="<%= yesterday_str %>" onChange="recalc_date()" />
				to
				<input type="text" name="textTo" id="textTo" value="<%= today_str %>" onChange="recalc_date()" />
                                <button id="updateButton"> Submit </button>
				<%-- <button id="saveButton"> Save </button>  --%>
                        </div>
			<div id="resizable" class="ui-widget-content" style="border-style:solid;height:400px;width:800px"><iframe src="contents.html" id="myfr" width="100%" height="100%" frameborder="0" marginheight="0" marginwidth="0"></iframe></div>
                    </td>
                    <td style="verticalAlign: top">
                        <div id="accordion"> 
                            <%--
                            <h3> <a href="#"> APPS </a> </h3>
                            <div>
                            <c:forEach var="pgm" items="${solrData.programs}">
                                <input type="checkbox" name="app" value="<c:out value="${pgm}" />" checked="checked" onClick="recalc_programs(); generate_solr_url()"/> <c:out value="${pgm}" /> <br />
                            </c:forEach>
                            </div>
                             --%>   
                            <h3><a href="#"> LEVEL </a></h3>
			
                            <div>
                            <c:forEach var="pgm" items="${solrData.levels}">
                                <input type="checkbox" name="lvl" value="<c:out value="${pgm}" />" checked="checked" onClick="recalc_level(); generate_solr_url()" /> <c:out value="${pgm}" /> <br />
                            </c:forEach>
                            </div>

                            <h3><a href="#"> HOSTNAME </a></h3>
			
                            <div>
                            <c:forEach var="pgm" items="${solrData.hostnames}">
                                <input type="checkbox" name="hostname" value="<c:out value="${pgm}" />" onClick="recalc_hostname(); generate_solr_url()" /> <c:out value="${pgm}" /> <br />
                            </c:forEach>
                            </div>
                        </div>  
                    </td>
                </tr>
            </table>
        </form>
        
    </body>
</html>
