<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<template:page pageTitle="${pageTitle}">
<vx-breadcrumb :breadcrumbs='${breadcrumbs}'></vx-breadcrumb>
	<div class="row checkout-container">
		<cms:pageSlot position="LeftContentSlot" var="feature" element="div" class="checkout-login-left-content-slot">
			<cms:component component="${feature}" element="div" class="checkout-login-left-content-component" />
		</cms:pageSlot>	
	</div>
	<%--Paypal Checkout Js  --%>
		<script src="https://www.paypalobjects.com/api/checkout.js"></script>
</template:page>