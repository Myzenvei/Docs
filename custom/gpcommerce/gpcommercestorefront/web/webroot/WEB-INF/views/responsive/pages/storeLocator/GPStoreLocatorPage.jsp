<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">

	<div class="container__full container-fluid">
		<div class=" row">

			<cms:pageSlot position="StoreLocatorSlot" var="feature" element="div" class="">

				<cms:component component="${feature}" />
			</cms:pageSlot>

			<cms:pageSlot position="BrandsHolderSlot" var="feature" element="div" class="col-xs-12">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
	</div>


</template:page>