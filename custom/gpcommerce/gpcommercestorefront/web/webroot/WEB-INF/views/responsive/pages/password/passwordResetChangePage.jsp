<%@ page trimDirectiveWhitespaces="true" %>
	<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template" %>
		<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user" %>
			<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
				<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

					<c:url value="/j_spring_security_check" var="loginActionUrl" />

					<spring:theme code="resetPwd.title" var="pageTitle" />

					<template:page pageTitle="${pageTitle}">
					<div class="row">
						<div class="col-xs-12 update-password-page d-flex flex-row flex-wrap">
							<!-- i18n for update password through props-->
							<vx-update-password class="update-password-component px-sm-5 px-xs-0 py-sm-5 py-xs-0 col-md-5 col-sm-8 col-xs-12" action-url='${loginActionUrl}'
							 :i18n="messages.registrationLogin.updatePassword"></vx-update-password>
						</div>
					</div>
					</template:page>