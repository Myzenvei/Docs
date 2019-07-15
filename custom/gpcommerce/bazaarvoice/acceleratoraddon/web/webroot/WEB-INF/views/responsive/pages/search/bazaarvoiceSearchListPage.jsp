<%@ page trimDirectiveWhitespaces="true" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/template" %>
<%--BAZAARVOICE_MODIFICATION_END--%>

<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%--<template:page pageTitle="${pageTitle}">--%>
<template:page pageTitle="${pageTitle}" showBV="true">
	<%--BAZAARVOICE_MODIFICATION_END--%>

	<div class="row">
		<div class="col-xs-3">
			<cms:pageSlot position="ProductLeftRefinements" var="feature" element="div" class="search-list-page-left-refinements-slot">
				<cms:component component="${feature}" element="div" class="search-list-page-left-refinements-component"/>
			</cms:pageSlot>
		</div>
		<div class="col-sm-12 col-md-9">
			<cms:pageSlot position="SearchResultsListSlot" var="feature" element="div" class="search-list-page-right-result-list-slot">
				<cms:component component="${feature}" element="div" class="search-list-page-right-result-list-component"/>
			</cms:pageSlot>
        </div>
	</div>

	<storepickup:pickupStorePopup />

</template:page>