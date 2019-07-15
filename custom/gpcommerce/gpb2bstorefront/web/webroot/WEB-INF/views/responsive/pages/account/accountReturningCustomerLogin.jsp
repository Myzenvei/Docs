<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	<%@ taglib prefix="user" tagdir="/WEB-INF/tags/responsive/user"%>

		<c:url value="/j_spring_security_check" var="loginActionUrl" />

		<!-- <div class="login-section">
	<user:login actionNameKey="login.login" action="${loginActionUrl}" />
</div> -->

		<!-- i18n for login through props-->
		<vx-login action-url='${loginActionUrl}' :i18n="messages.registrationLogin.login"></vx-login>