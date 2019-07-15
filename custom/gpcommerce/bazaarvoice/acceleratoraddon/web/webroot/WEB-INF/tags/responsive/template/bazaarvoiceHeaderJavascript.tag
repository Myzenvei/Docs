<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="zone" value="${cmsSite.bvConfig.zone}" />
<jsp:useBean id="zone" class="java.lang.String" />
<%
	String normalizedZone =java.net.URLEncoder.encode(zone,"UTF-8").replace("+", "_").toLowerCase();
%>

<c:set var="environment" value="${cmsSite.bvConfig.environment.code}" />
<jsp:useBean id="environment" class="java.lang.String" />
<%
	String env = environment.toLowerCase();
%>
<%@ tag trimDirectiveWhitespaces="true" %>
<script type="text/javascript" async src="https://apps.bazaarvoice.com/deployments/${cmsSite.bvConfig.clientName}/<%=normalizedZone%>/<%=env%>/${cmsSite.bvLocale}/bv.js"></script>
