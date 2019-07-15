<%@ tag body-content="scriptless" trimDirectiveWhitespaces="true"%>
<%@ attribute name="pageTitle" required="false" rtexprvalue="true"%>
<%@ attribute name="pageCss" required="false" fragment="true"%>
<%@ attribute name="pageScripts" required="false" fragment="true"%>
<%@ attribute name="hideHeaderLinks" required="false"%>

<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="header" tagdir="/WEB-INF/tags/responsive/common/header"%>
<%@ taglib prefix="footer" tagdir="/WEB-INF/tags/responsive/common/footer"%>
<%@ taglib prefix="common" tagdir="/WEB-INF/tags/responsive/common"%>
<%@ taglib prefix="cart" tagdir="/WEB-INF/tags/responsive/cart" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<spring:htmlEscape defaultHtmlEscape="true" />

<template:master pageTitle="${pageTitle}">

	<jsp:attribute name="pageCss">
		<jsp:invoke fragment="pageCss" />
	</jsp:attribute>

	<jsp:attribute name="pageScripts">
		<jsp:invoke fragment="pageScripts" />
	</jsp:attribute>

	<jsp:body>

		<c:set var = "string2" value = "${fn:split(user.uid, '|')}" />
		<c:choose>
			 <c:when test="${isAsmAgentLoggedIn == true && string2[0] == 'anonymous'}" >
			 	<c:set var = "originalUid" value = "asm_anonymous" />
			 </c:when>
			 <c:when test="${isAsmAgentLoggedIn == true && string2[0] != 'anonymous'}" >
			 	<c:set var = "originalUid" value = "asm_loggedin" />
			 </c:when>
			 <c:otherwise>
		<c:set var = "originalUid" value = "${string2[0]}" />
			 </c:otherwise>
		</c:choose>

		<%-- conditions to load banner specific ids--%>
		<c:choose>
			<c:when test="${siteUid == 'gppro'}">
				<div id="gp-pro">
			</c:when>
			<c:when test="${siteUid == 'mardigras'}">
				<div id="mardi-gras">
			</c:when>
			<c:when test="${siteUid == 'vanityfair'}">
				<div id="vanity-fair">
			</c:when>
			<c:when test="${siteUid == 'dixie'}">
				<div id="dixie">
			</c:when>
			<c:when test="${siteUid == 'b2bwhitelabel'}">
				<div id="b2b-white-label">
			</c:when>
			<c:when test="${siteUid == 'gpxpress'}">
				<div id="gp-xpress">
			</c:when>
			<c:when test="${siteUid == 'gpemployee'}">
				<div id="gp-employee">
			</c:when>
			<c:otherwise>
				<div id="gp-pro">
			</c:otherwise>
		</c:choose>

			<a class="skip-main" href="#main">Skip to main content</a>
			<vx-assistive-text ref='assistiveText'></vx-assistive-text>

			<vx-customer-details
				uid="${originalUid}"
				first-name="${user.firstName}"
				last-name="${user.lastName}"
				email="${user.displayUid}"
				contact-number="${user.contactNumber}"
				unit="${user.unit.getUid()}"
				user-roles="${user.roles}"
				b2b-unit-level="${user.b2bUnitLevel}"
        		b2b-user-review-status="${user.inReview}"
				customer-id="${user.customerId}"
				asm-launch-mode="${isAsmModeLaunched}"
				asm-loggedin="${isAsmAgentLoggedIn}" user-email="${user.email}">
			</vx-customer-details>

			<vx-application-auth
				site-id="${siteUid}"
				site-type="B2B"
				current-page="${cmsPage.uid}"
				google-maps-key="${googleMapsApiKey}"
				locale="en"
				common-resource-path="${commonResourcePath}"
				context-path="${contextPath}"
				csrf-token='${CSRFToken.token}'
				:navigations='${navigations}'
				:third-party-apps='${thirdPartyApps}'
				:show-paypal='${showPaypal}'
				paypal-env='${paypalEnv}'
				:application-auth-params='${applicationAuthParams}'
				:site-config='${gpSiteConfig}'>
			</vx-application-auth>



			<div v-if='globals.hasGlobalAuth || globals.loggedIn'>
				<vx-cart-guid></vx-cart-guid>
				<vx-video-player></vx-video-player>

				<header:header hideHeaderLinks="${hideHeaderLinks}" />
				<vx-flyout-banner ref="flyoutBanner"></vx-flyout-banner>
				<vx-flyout-banner-secondary ref="flyoutBannerSecondary"></vx-flyout-banner-secondary>

				<c:choose>
					<c:when test="${siteUid == 'gppro'}">
						<div class="main__inner-wrapper" id='main' role="main" tabindex="-1">
							<jsp:doBody />
							<vx-live-agent-btn :i18n="messages.accessCustomerService.liveChat" :all-page-live-chat='${showLiveChat}' />
						</div>
					</c:when>
					<c:otherwise>
							<c:choose>
								<c:when test="${originalUid == 'asm_anonymous'}">
									<div class="main__inner-wrapper" id='main' role="main" tabindex="-1">
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${originalUid == 'asm_anonymous'}">
											<div class="main__inner-wrapper" id='main' role="main" tabindex="-1">
										</c:when>
										<c:otherwise>
						<div class="main__inner-wrapper" id='main' role="main" tabindex="-1" v-if='cartExists'>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<jsp:doBody />
							<vx-live-agent-btn :i18n="messages.accessCustomerService.liveChat" :all-page-live-chat='${showLiveChat}' />
						</div>
					</c:otherwise>
				</c:choose>


				<footer:footer />
			</div>

		</div>
		<c:choose>
      <c:when test="${fn:containsIgnoreCase(cmsPage.typeCode,'ContentPage') and (fn:containsIgnoreCase(cmsPage.uid,'HomePage') or fn:containsIgnoreCase(cmsPage.label,'HomePage'))}">
         <div itemscope style="display: none;"
                        itemtype="http://schema.org/Organization">
            <span itemprop="name">"${not empty pageTitle ? pageTitle : siteUid}"</span>
            <span itemprop="legalName">"${not empty pageTitle ? pageTitle : siteUid}"</span>
            <span itemprop="url">"${canonicalUrl}"</span>
         </div>
      </c:when>
      </c:choose>
	</jsp:body>


</template:master>
