<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%@ taglib prefix="bazaarvoice" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/product"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/addons/bazaarvoice/responsive/template"%>
<%--BAZAARVOICE_MODIFICATION_END--%>

<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>

<%--BAZAARVOICE_MODIFICATION_START--%>
<%--<template:page pageTitle="${pageTitle}">--%>
<template:page pageTitle="${pageTitle}" showBV="true">
	<%--BAZAARVOICE_MODIFICATION_END--%>
	<cms:pageSlot position="Section1" var="comp" element="div" class="productDetailsPageSection1">
		<cms:component component="${comp}" element="div" class="productDetailsPageSection1-component"/>
	</cms:pageSlot>

	<%--BAZAARVOICE_MODIFICATION_START--%>
	<bazaarvoice:bazaarvoiceProductDetailsPanel />
	<%--BAZAARVOICE_MODIFICATION_END--%>

	<cms:pageSlot position="CrossSelling" var="comp" element="div" class="productDetailsPageSectionCrossSelling">
		<cms:component component="${comp}" element="div" class="productDetailsPageSectionCrossSelling-component"/>
	</cms:pageSlot>
	<cms:pageSlot position="Section3" var="comp" element="div" class="productDetailsPageSection3">
		<cms:component component="${comp}" element="div" class="productDetailsPageSection3-component"/>
	</cms:pageSlot>
	<cms:pageSlot position="UpSelling" var="comp" element="div" class="productDetailsPageSectionUpSelling">
		<cms:component component="${comp}" element="div" class="productDetailsPageSectionUpSelling-component"/>
	</cms:pageSlot>
	<product:productPageTabs />
	<cms:pageSlot position="Section4" var="comp" element="div" class="productDetailsPageSection4">
		<cms:component component="${comp}" element="div" class="productDetailsPageSection4-component"/>
	</cms:pageSlot>
</template:page>