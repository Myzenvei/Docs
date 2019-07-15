<%@ page trimDirectiveWhitespaces="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
			<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
				<template:page pageTitle="${pageTitle}">
				<c:if test="${siteUid == 'gpemployee'}">
					<div class="list-details-page">
				</c:if>
				<c:if test="${isFavoritesEnabled eq false && isAnonymous eq false}">
				<vx-breadcrumb :breadcrumbs='${breadcrumbs}'></vx-breadcrumb>
				</c:if>
					<div class="wrapper d-flex">
						<c:if test="${isFavoritesEnabled eq true}">
							<div class="left col-md-3 hidden-sm hidden-xs">
								<cms:pageSlot position="Section1" var="feature">
									<cms:component component="${feature}" />
								</cms:pageSlot>
							</div>
						</c:if>
						<div class="right col-xs-12 col-sm-12 col-md-12 mx-sm-3 mx-md-5">
							<cms:pageSlot position="GPListdetailsSlot" var="feature" element="div" class="">
								<cms:component component="${feature}" />
							</cms:pageSlot>
						</div>
					</div>
				<c:if test="${siteUid == 'gpemployee'}">
					</div>
				</c:if>
				</template:page>
