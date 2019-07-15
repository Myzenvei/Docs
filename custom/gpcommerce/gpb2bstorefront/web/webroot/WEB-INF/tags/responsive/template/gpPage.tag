<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:master pageTitle="${pageTitle}">

	<jsp:attribute name="pageCss">
		<jsp:invoke fragment="pageCss" />
	</jsp:attribute>

	<jsp:attribute name="pageScripts">
		<jsp:invoke fragment="pageScripts" />
	</jsp:attribute>

	<jsp:body>
	
	<c:set var="string2" value="${fn:split(user.uid, '|')}" />
	<c:set var="originalUid" value="${string2[0]}" />
	
		<div id="app">
			<vx-application-auth site-id="${siteUid}"
				google-maps-key="${googleMapsApiKey}"
				site-type="B2B" locale="en"
				common-resource-path="${commonResourcePath}"
				context-path="${contextPath}" csrf-token='${CSRFToken.token}'
				uid='${originalUid}'></vx-application-auth>


		<%-- <main data-currency-iso-code="${fn:escapeXml(currentCurrency.isocode)}">
			<spring:theme code="text.skipToContent" var="skipToContent" />
			<a href="#skip-to-content" class="skiptocontent" data-role="none">${fn:escapeXml(skipToContent)}</a>
			<spring:theme code="text.skipToNavigation" var="skipToNavigation" />
			<a href="#skiptonavigation" class="skiptonavigation" data-role="none">${fn:escapeXml(skipToNavigation)}</a> --%>

			<vx-cart-guid></vx-cart-guid>
			<vx-video-player></vx-video-player>
			<a id="skip-to-content"></a>

			<div class="main__inner-wrapper">
				<%-- <common:globalMessages />
				<cart:cartRestoration /> --%>
				<jsp:doBody />
			</div>
		
		<%-- </main> --%>
</div>
	</jsp:body>

</template:master>