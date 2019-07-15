<%@ page trimDirectiveWhitespaces="true" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%--<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>--%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/template" %>
<%--BAZAARVOICE_MODIFICATION_END--%>

<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags" %>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common" %>
<%@ taglib prefix="storepickup" tagdir="/WEB-INF/tags/responsive/storepickup" %>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%--<template:page pageTitle="${pageTitle}">--%>
<template:page pageTitle="${pageTitle}" showBV="true">
	<%--BAZAARVOICE_MODIFICATION_END--%>

	<div class="row">		
		<cms:pageSlot position="Section1" var="feature" element="div" class="product-list-section1-slot">
			<cms:component component="${feature}" element="div" class="col-xs-12 yComponentWrapper product-list-section1-component"/>
		</cms:pageSlot>		
	</div>
	<div class="row">
		<div class="col-xs-3">
			<cms:pageSlot position="ProductLeftRefinements" var="feature" element="div" class="product-list-left-refinements-slot">
				<cms:component component="${feature}" element="div" class="yComponentWrapper product-list-left-refinements-component"/>
			</cms:pageSlot>
		</div>
		<div class="col-sm-12 col-md-9">
			<cms:pageSlot position="ProductListSlot" var="feature" element="div" class="product-list-right-slot">
				<cms:component component="${feature}" element="div" class="product__list--wrapper yComponentWrapper product-list-right-component"/>
			</cms:pageSlot>
		</div>
	</div>

</template:page>