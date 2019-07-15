<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:theme code="resetPwd.token.expire.title" var="pageTitle" />

<template:page pageTitle="${pageTitle}">
	<div class="col-xs-12 expiry-password-page d-flex flex-row flex-wrap">
		<!-- i18n for forget password through props-->
		<vx-forgot-password class="expiry-password-component px-sm-5 px-xs-0 pb-sm-5 py-xs-0 col-md-4 col-sm-8 col-xs-12"
		 :i18n="messages.registrationLogin.passwordExpiry" is-expiry></vx-forgot-password>
	</div>
</template:page>