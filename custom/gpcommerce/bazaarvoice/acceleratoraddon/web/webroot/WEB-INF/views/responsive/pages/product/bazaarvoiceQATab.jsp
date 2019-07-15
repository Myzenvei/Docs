<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%@ taglib prefix="bazaarvoice"
		   tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/bazaarvoice" %>
<%--BAZAARVOICE_MODIFICATION_END--%>

<div id="tabqa" class="tabhead">
	<a href="">${fn:escapeXml(title)}</a> <span class="glyphicon"></span>
</div>
<div class="tabbody">
	<div class="container-lg">
		<%--BAZAARVOICE_MODIFICATION_START--%>
		<bazaarvoice:productPageQuestionAndAnswerTab product="${product}" />
		<%--BAZAARVOICE_MODIFICATION_END--%>
	</div>
</div>

