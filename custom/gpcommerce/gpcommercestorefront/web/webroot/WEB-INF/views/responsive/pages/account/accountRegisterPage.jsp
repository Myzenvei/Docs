<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
	<div class="row register-page" v-if="!globals.isEmployeeStore()">
		<div class="col-md-6 col-sm-7 col-xs-12 my-md-5 my-sm-5 my-xs-0 px-md-3 px-xs-0 register-component d-flex flex-row flex-wrap">
			<div class="col-md-8 col-sm-10 col-xs-12 mx-xs-0 my-xs-0 px-xs-0 register-left-content-slot">
				<cms:pageSlot position="LeftContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
		<div class="col-md-6 col-sm-5 col-xs-12 my-md-5 my-sm-5 mt-xs-3 px-md-3 px-xs-0 social-login-component d-flex flex-row flex-wrap">
			<div class="col-md-6 col-sm-10 col-xs-12 mx-xs-0 my-xs-0 px-xs-0 register-right-content-slot">
				<cms:pageSlot position="RightContentSlot" var="feature">
					<cms:component component="${feature}" />
				</cms:pageSlot>
			</div>
		</div>
	</div>
	<template v-if="!globals.isEmployeeStore()">
		<p class="my-xs-5 pb-xs-2 support d-flex justify-content-center" tabindex="0" role="text">{{messages.registrationLogin.socialLogin.support}}&nbsp;
			<a href="tel:${messages.registrationLogin.socialLogin.supportNumber}">
				{{messages.registrationLogin.socialLogin.supportNumber}}</a></p>
	</template>
	<!-- This is temporary solution, there should be new layout for this -->
	<div class=" row business-login-page" v-if="globals.isEmployeeStore()">
		<div class="d-flex business-register-component flex-row flex-wrap">
			<cms:pageSlot position="LeftContentSlot" var="feature" class="col-md-4 col-sm-6 col-xs-12 px-sm-5 pt-sm-5 pb-sm-3 mx-sm-0 business-login">
				<cms:component component="${feature}" />
			</cms:pageSlot>
		</div>
		<div class="my-xs-5 pb-xs-2 support d-flex flex-column align-center" tabindex="0" role="text">
			<p v-html="messages.registrationLogin.socialLogin.support"></p>
			<a href="tel:${messages.registrationLogin.socialLogin.supportNumber}" v-html="messages.registrationLogin.socialLogin.supportNumber"></a>
		</div>
	</div>
</template:page>