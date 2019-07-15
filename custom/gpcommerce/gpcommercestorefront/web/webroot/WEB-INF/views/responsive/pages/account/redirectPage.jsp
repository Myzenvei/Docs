<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
		<div class="col-md-12">
			<cms:pageSlot position="CenterContentSlot" var="feature" >
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
</template:page>
