<%@ page trimDirectiveWhitespaces="true"%>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
		<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
			<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>
				<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
					<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
						<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
							<spring:url value="/my-account/update-profile" var="updateProfileUrl" />
							<spring:url value="/my-account/update-password" var="updatePasswordUrl" />
							<spring:url value="/my-account/update-email" var="updateEmailUrl" />
							<spring:url value="/my-account/address-book" var="addressBookUrl" />
							<spring:url value="/my-account/payment-details" var="paymentDetailsUrl" />
							<spring:url value="/my-account/orders" var="ordersUrl" />
							<template:page pageTitle="${pageTitle}">
								<div class="wrapper d-flex">
									<c:choose>
										<c:when test="${fn:escapeXml(cmsPage.uid) eq 'order'}">
											<div class="col-xs-12 px-xs-0 order-details-container">
												<div class="account-section">
													<cms:pageSlot position="BodyContent" var="feature" element="div" class="account-section-content">
														<cms:component component="${feature}" />
													</cms:pageSlot>
												</div>
											</div>
										</c:when>
										<c:otherwise>
											<div class="left col-md-3 hidden-sm hidden-xs">
												<cms:pageSlot position="Section1" var="feature">
													<cms:component component="${feature}" />
												</cms:pageSlot>
											</div>
											<div class="right col-xs-12 col-sm-12 col-md-9">
												<cms:pageSlot position="SideContent" var="feature" class="accountPageSideContent">
													<cms:component component="${feature}" />
												</cms:pageSlot>
												<cms:pageSlot position="TopContent" var="feature" element="div" class="accountPageTopContent">
													<cms:component component="${feature}" />
												</cms:pageSlot>
												<div class="account-section">
													<cms:pageSlot position="BodyContent" var="feature" element="div" class="account-section-content">
														<cms:component component="${feature}" />
													</cms:pageSlot>
												</div>
											</div>
										</c:otherwise>
									</c:choose>
								</div>
							</template:page>
