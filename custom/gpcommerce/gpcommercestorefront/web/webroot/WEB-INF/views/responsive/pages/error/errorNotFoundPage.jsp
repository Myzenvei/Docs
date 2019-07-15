<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<template:page pageTitle="${pageTitle}">
	<div class="container-fluid">
		<c:choose>
			
			<c:when test="${siteUid == 'mardigras'}">
				<mg-404 :i18n = "messages.fileNotFoundError"></mg-404>
			</c:when>
			<c:otherwise>
				<vx-404-page :i18n="messages.nfr.i18n404" class='p-xs-5'></vx-404-page>
			</c:otherwise>
		</c:choose>
	</div>
</template:page>