<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
	<div class="wrapper d-flex">
		<div class="left col-md-3 hidden-xs hidden-sm">
			<cms:pageSlot position="Section1" var="feature">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		<div class="right col-xs-12 col-sm-12 col-md-9">
			<cms:pageSlot position="GPOrdersApprovalSlot" var="feature" element="div"
				class="">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>
</template:page>
