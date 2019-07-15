<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<template:page pageTitle="${pageTitle}">
	<template v-if="globals.getIsSubscription()">
		<p class="subscribeText mb-md-4 mx-xs-3 mx-sm-0 mb-xs-3 mb-sm-0" tabindex="0" role="text">{{messages.registrationLogin.login.subscribeText}}</p>
	</template>	
	<div class="row login-page" v-if="!globals.isEmployeeStore()" :class="{'checkout-login' : globals.getCookie('flow') == 'checkout'}">
		<template v-if="globals.getCookie('flow') == 'checkout'">
			<div class="col-md-6 col-sm-6 col-xs-12 my-md-5 my-sm-5 my-xs-0 px-xs-0 login-component d-flex flex-row flex-wrap">
				<cms:pageSlot position="LeftContentSlot" var="feature" element="div" class="col-md-8 col-sm-10 col-xs-12  mx-xs-0 px-md-2 px-xs-0 login-left-content-slot">
					<cms:component component="${feature}" element="div" class="login-left-content-component" />
				</cms:pageSlot>
				<cms:pageSlot position="RightContentSlot" var="feature" element="div" class="col-md-8 col-sm-10 col-xs-12  mx-xs-0 px-md-2 px-xs-0 login-right-content-slot">
					<cms:component component="${feature}" element="div" class="login-right-content-component" />
				</cms:pageSlot>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-12 my-md-5 my-sm-5 mt-xs-3 px-xs-0 social-login-component d-flex flex-row flex-wrap">
				<cms:pageSlot position="PlaceholderContentSlot" var="feature" element="div" class="col-md-8 col-sm-10 col-xs-12  mx-xs-0 px-md-2 px-xs-0 login-right-content-slot">
					<cms:component component="${feature}" element="div" class="login-right-content-component" />
				</cms:pageSlot>
			</div>
		</template>
		<template v-else>
			<div class="col-md-6 col-sm-6 col-xs-12 my-md-5 my-sm-5 my-xs-0 px-xs-0 login-component d-flex flex-row flex-wrap">
				<cms:pageSlot position="LeftContentSlot" var="feature" element="div" class="col-md-8 col-sm-10 col-xs-12  mx-xs-0 px-md-2 px-xs-0 login-left-content-slot">
					<cms:component component="${feature}" element="div" class="login-left-content-component" />
				</cms:pageSlot>
			</div>
			<div class="col-md-6 col-sm-6 col-xs-12 my-md-5 my-sm-5 mt-xs-3 px-xs-0 social-login-component d-flex flex-row flex-wrap">
				<cms:pageSlot position="RightContentSlot" var="feature" element="div" class="col-md-6 col-sm-10 col-xs-12  px-md-2 px-xs-0 mx-xs-0 login-right-content-slot">
					<cms:component component="${feature}" element="div" class="login-right-content-component" />
				</cms:pageSlot>
			</div>
		</template>

	</div>
	<!-- This is a for testing Emp store -->
	<div class="row business-login-page" v-if="globals.isEmployeeStore()">
		<div class="d-flex login-component flex-row flex-wrap">
			<cms:pageSlot position="LeftContentSlot" var="feature" element="div" class="col-md-4 col-sm-6 col-xs-12 px-sm-5 pt-sm-5 pb-sm-3 mx-sm-0 business-login login-left-content-slot">
				<cms:component component="${feature}" element="div" class="login-left-content-component" />
			</cms:pageSlot>
		</div>
	</div>
	<template v-if="globals.getCookie('flow') !== 'checkout'">
		<p class="my-xs-5 pb-xs-2 support d-flex justify-content-center">{{messages.registrationLogin.socialLogin.support}}&nbsp;
			<a href="tel:${messages.registrationLogin.socialLogin.supportNumber}">
				{{messages.registrationLogin.socialLogin.supportNumber}}</a></p>
	</template>
</template:page>
